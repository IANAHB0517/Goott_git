package com.miniproj.board.service;

import java.io.IOException;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.miniproj.board.controller.BoardFactory;

public interface BoardService {
	BoardFactory action(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException;
}
