package rpc;
//RPC: Remote Procedure Call

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;
import external.TicketMasterAPI;
import java.util.*;

/**
 * Servlet implementation class SearchItem
 */
@WebServlet("/search")
public class SearchItem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchItem() {
        super();
        // TODO Auto-generated constructor stub
        // 检查一下这里的代码！！！！！！！！！
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/** PrintWriter out = response.getWriter(); //store a buffer in "response" for message 在response里开一个buffer来存信息
//		if (request.getParameter("username") != null) {
//			//Get the username sent from the client
//			String username = request.getParameter("username");
//            //In the output stream, add something to response body. 
//			out.print("Hello " + username);
//		}
//		// Send response back to client
//		out.flush(); //把写好的东西通过response冲出去给前端
//		out.close(); //相当于delete pointer
		
		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");

		String username = "";
		if (request.getParameter("username") != null) {
			username = request.getParameter("username");
		}
		JSONObject obj = new JSONObject(); //关键stuff，这是java class
		JSONArray array = new JSONArray();
		try {
			array.put(new JSONObject().put("username", "abcd"));
			array.put(new JSONObject().put("username", "1234"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		out.print(array);
		out.flush();
		out.close(); **/
		
		
		//这里是一个学习factory design pattern的实际例子
		// Get parameter from HTTP request
		/*
		double lat = Double.parseDouble(request.getParameter("lat"));
		double lon = Double.parseDouble(request.getParameter("lon"));
		String term = request.getParameter("term"); // term can be null

		// call TicketMasterAPI.search to get event data
		TicketMasterAPI tmAPI = new TicketMasterAPI();
		List<Item> items = tmAPI.search(lat, lon, term);

		// There should be some saveItem logic here

		// Convert Item list back to JSONArray for client
		List<JSONObject> list = new ArrayList<>();
		try {
			for (Item item : items) {
				JSONObject obj = item.toJSONObject();
				list.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONArray array = new JSONArray(items);
		RpcHelper.writeJsonArray(response, array);
		*/
		
		String userId = request.getParameter("user_id");
		double lat = Double.parseDouble(request.getParameter("lat"));
		double lon = Double.parseDouble(request.getParameter("lon"));
		String term = request.getParameter("term"); // Term can be empty or null.
		
		DBConnection conn = DBConnectionFactory.getDBConnection();
		List<Item> items = conn.searchItems(userId, lat, lon, term);
		List<JSONObject> list = new ArrayList<>();

		Set<String> favorite = conn.getFavoriteItemIds(userId);
		try {
			for (Item item : items) {
				JSONObject obj = item.toJSONObject();
				if (favorite != null) {
					obj.put("favorite", favorite.contains(item.getItemId()));
				}
				list.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONArray array = new JSONArray(list);
		RpcHelper.writeJsonArray(response, array);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
