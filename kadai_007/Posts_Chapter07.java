package kadai_007;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Posts_Chapter07 {
    public static void main(String[] args) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet result = null;

        String[][] postsList = {
            {"1003","昨日の夜は徹夜でした・・","13"},
            {"1002","お疲れ様です！","12"},
            {"1003","今日も頑張ります！","18"},
            {"1001","無理は禁物ですよ！","17"},
            {"1002","明日から連休ですね！","20"}
        };

        try {
            // データベースに接続
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost/challenge_java",
                "root",
                "96261127"
            );

            System.out.println("データベース接続成功");
            

            // レコード追加を実行します
            System.out.println("レコード追加を実行します");
            String insertSql = "INSERT INTO posts (user_id, posted_at, post_content, likes) VALUES (?, ?, ?, ?)";
            pstmt = con.prepareStatement(insertSql);

            Date currentDate = Date.valueOf(LocalDate.now()); // 現在の日付を取得

            for (String[] post : postsList) {
                pstmt.setString(1, post[0]);
                pstmt.setDate(2, currentDate);
                pstmt.setString(3, post[1]);
                pstmt.setString(4, post[2]);
                pstmt.executeUpdate();
            }
            System.out.println(postsList.length + "件のレコードが追加されました");

            // ユーザーIDが1002の投稿を検索
            System.out.println("ユーザーIDが1002のレコードを検索しました");
            String selectSql = "SELECT posted_at, post_content, likes FROM posts WHERE user_id = 1002";
            pstmt = con.prepareStatement(selectSql);
            result = pstmt.executeQuery();

            int count = 1;
            while (result.next()) {
                Date postedAt = result.getDate("posted_at"); // getDate()を使用
                String postContent = result.getString("post_content");
                int likes = result.getInt("likes");
                System.out.println(count + "件目：投稿日時=" + postedAt + "／投稿内容=" + postContent + "／いいね数=" + likes);
                count++;
            }

        } catch (SQLException e) {
            System.out.println("エラー発生：" + e.getMessage());
        } finally {
            // 使用したオブジェクトを解放
            try {
                if (result != null) result.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException ignore) {}
        }
    }
}