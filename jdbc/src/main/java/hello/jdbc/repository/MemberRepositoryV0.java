package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

@Slf4j
public class MemberRepositoryV0 {
    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values(?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch(SQLException e) {
            log.error("DB ERROR", e);
            throw e;
        } finally {
            close(pstmt, con);
        }
    }

    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();
            if(rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found member_id = " + memberId);
            }
        } catch(SQLException e) {
            log.error("DB ERROR", e);
            throw e;
        } finally {
            close(rs, pstmt, con);
        }
    }

    public int update(String memberId, int money) throws SQLException {
        String sql = "update member set money = ? where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            return pstmt.executeUpdate();
        } catch(SQLException e) {
            log.error("DB ERROR", e);
            throw e;
        } finally {
            close(pstmt, con);
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            log.error("DB ERROR", e);
            throw e;
        } finally {
            close(pstmt, con);
        }
    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }

    public void close(AutoCloseable... objects) {
        Arrays.stream(objects)
                .filter(Objects::nonNull)
                .forEach(object -> {
                    try {
                        //log.info("close object : {}", object.getClass());
                        object.close();
                    } catch(Exception e) {
                        log.error("ERROR", e);
                    }
                });
    }
}
