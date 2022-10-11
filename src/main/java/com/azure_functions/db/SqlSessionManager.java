package com.azure_functions.db;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;
import java.util.function.Function;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.microsoft.azure.functions.ExecutionContext;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SqlSessionManager {

  /** SQLセッションファクトリー */
  private static SqlSessionFactory sqlSessionFactory = sqlSessionFactory();

  /** 実行コンテキスト */
  private final ExecutionContext context;

  private static SqlSessionFactory sqlSessionFactory() {
    // DB接続情報を環境変数から取得
    Properties mybatisProps = new Properties();
    mybatisProps.put("url", System.getenv("MYSQL_URL"));
    mybatisProps.put("username", System.getenv("MYSQL_USER"));
    mybatisProps.put("password", System.getenv("MYSQL_PASSWORD"));

    try (Reader config = Resources.getResourceAsReader("mybatis-config.xml")) {
      SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(config, mybatisProps);
      return sqlSessionFactory;

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 指定したステートメントをトランザクション内で実行する
   * 
   * @param <R>       戻り値の型
   * @param statement ステートメント
   * @return SQL実行結果
   */
  public <R> R transaction(Function<SqlSession, R> statement) {
    SqlSession sqlSession = sqlSessionFactory.openSession(false);
    try (sqlSession) {
      // ステートメント実行
      R result = statement.apply(sqlSession);

      // コミット
      sqlSession.commit();

      return result;

    } catch (Exception e) {
      context.getLogger().severe("ステートメントの実行に失敗しました。エラーメッセージ：" + e.getMessage());

      // ロールバック
      sqlSession.rollback();

      throw e;
    }
  }

}
