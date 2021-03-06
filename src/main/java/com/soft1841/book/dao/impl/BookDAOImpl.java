package com.soft1841.book.dao.impl;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.sql.Condition;
import com.soft1841.book.dao.BookDAO;
import com.soft1841.book.entity.Book;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAOImpl implements BookDAO {
    @Override
    public Long insertBook(Book book) throws SQLException {
        return Db.use().insertForGeneratedKey(
                Entity.create("t_book")
                        .set("type_id", book.getTypeId())
                        .set("name", book.getName())
                        .set("author", book.getAuthor())
                        .set("price", book.getPrice())
                        .set("cover", book.getCover())
                        .set("summary", book.getSummary())
                        .set("stock", book.getStock())
        );
    }

    @Override
    public int deleteBookById(long id) throws SQLException {
        return Db.use().del(
                Entity.create("t_book").set("id", id)
        );
    }

    @Override
    public int updateBook(Book book) throws SQLException {
        //只修改了图书的价格和库存
        return Db.use().update(
                Entity.create().set("price", book.getPrice())
                        .set("stock", book.getStock()),
                Entity.create("t_book").set("id", book.getId())
        );
    }

    @Override
    public List<Book> selectAllBooks() throws SQLException {
        List<Entity> entityList = Db.use().query("SELECT * FROM t_book ");
        List<Book> bookList = new ArrayList<>();
        for (Entity entity : entityList) {
            bookList.add(convertBook(entity));
        }
        return bookList;
    }

    @Override
    public Book getBookById(long id) throws SQLException {
        Entity entity = Db.use().queryOne("SELECT * FROM t_book WHERE id = ? ", id);
        return convertBook(entity);
    }

    @Override
    public List<Book> selectBooksLike(String keywords) throws SQLException {
        List<Entity> entityList = Db.use().findLike("t_book", "name", keywords, Condition.LikeType.Contains);
        List<Book> bookList = new ArrayList<>();
        for (Entity entity : entityList) {
            bookList.add(convertBook(entity));
        }
        return bookList;
    }

    @Override
    public List<Book> selectBooksByTypeId(long typeId) throws SQLException {
        List<Entity> entityList = Db.use().query("SELECT * FROM t_book WHERE type_id = ? ", typeId);
        List<Book> bookList = new ArrayList<>();
        for (Entity entity : entityList) {
            bookList.add(convertBook(entity));
        }
        return bookList;
    }

    @Override
    public int countByType(long typeId) throws SQLException {
        return Db.use().queryNumber("SELECT COUNT(*) FROM t_book WHERE type_id = ? ", typeId).intValue();
    }

    @Override
    public int countBooks() throws SQLException {
        return Db.use().queryNumber("SELECT COUNT(*) FROM t_book ").intValue();

    }

    private Book convertBook(Entity entity) {
        Book book = new Book();
        book.setId(entity.getLong("id"));
        book.setTypeId(entity.getLong("type_id"));
        book.setName(entity.getStr("name"));
        book.setPrice(entity.getDouble("price"));
        book.setAuthor(entity.getStr("author"));
        book.setCover(entity.getStr("cover"));
        book.setSummary(entity.getStr("summary"));
        book.setStock(entity.getInt("stock"));
        return book;
    }
}
