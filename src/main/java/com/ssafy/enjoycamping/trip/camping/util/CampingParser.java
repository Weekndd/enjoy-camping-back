package com.ssafy.enjoycamping.trip.camping.util;

import com.ssafy.enjoycamping.trip.camping.entity.Camping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CampingParser {
//
//    public static void main(String[] args) throws IOException {
//        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B551011/GoCamping/basedList");
//        urlBuilder.append("?serviceKey=").append(URLEncoder.encode("5yz9Ov8+x9NqoUIr5d6RjfGcKmNSb5skVX/DvAJGGzCdEllR/oL6qGjcmERv1vO400+J3JnulMoD1AgD910xkQ==", "UTF-8"));
//        urlBuilder.append("&numOfRows=500");
//        urlBuilder.append("&pageNo=9");
//        urlBuilder.append("&MobileOS=ETC");
//        urlBuilder.append("&MobileApp=AppTest");
//        urlBuilder.append("&_type=xml");
//
//        System.out.println(urlBuilder.toString());
//
//        URL url = new URL(urlBuilder.toString());
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Content-type", "application/xml");
//        System.out.println("Response code: " + conn.getResponseCode());
//        BufferedReader rd;
//        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        } else {
//            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//        }
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = rd.readLine()) != null) {
//            sb.append(line);
//        }
//        rd.close();
//        conn.disconnect();
//        System.out.println(sb.toString());
//
//        List<Camping> campingList = parseCampingData(sb.toString());
//        insertCampingData(campingList);
//    }
//
//    private static void insertCampingData(List<Camping> campingList) {
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//
//        StringBuilder sql = new StringBuilder();
//        sql.append("INSERT INTO camping(name, sido_name, sido_code, gugun_name, gugun_code, ");
//        sql.append("detail_address, latitude, longitude, image_url, introduction, ");
//        sql.append("telephone, homepage_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
//
//        try {
//            conn = db.getConnection();
//            pstmt = conn.prepareStatement(sql.toString());
//
//            for (Camping camping : campingList) {
//                if (camping.getSidoCode() == 0 || camping.getGugunCode() == 0) {
////                	System.out.println("Skipping camping: " + camping.getName() + " due to invalid sido_code or gugun_code");
//                    continue;
//                }
//
//                if (isCampingExists(conn, camping.getName())) {
////                    System.out.println("Skipping duplicate camping: " + camping.getName());
//                    continue;
//                }
//
//                int index = 1;
//                pstmt.setString(index++, camping.getName());
//                pstmt.setString(index++, camping.getSidoName());
//                pstmt.setInt(index++, camping.getSidoCode());
//                pstmt.setString(index++, camping.getGugunName());
//                pstmt.setInt(index++, camping.getGugunCode());
//                pstmt.setString(index++, camping.getDetailAddress());
//                pstmt.setBigDecimal(index++, camping.getLatitude());
//                pstmt.setBigDecimal(index++, camping.getLongitude());
//                pstmt.setString(index++, camping.getImageUrl());
//                pstmt.setString(index++, camping.getIntroduction());
//                pstmt.setString(index++, camping.getTelephone());
//                pstmt.setString(index++, camping.getHomepageUrl());
//
//                pstmt.addBatch();
//            }
//
//            pstmt.executeBatch();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            db.close(pstmt, conn);
//        }
//    }
//
//    private static List<Camping> parseCampingData(String apiResponse) {
//        List<Camping> campingList = new ArrayList<>();
//
//        try {
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            InputStream is = new ByteArrayInputStream(apiResponse.getBytes("UTF-8"));
//            Document doc = builder.parse(is);
//            doc.getDocumentElement().normalize();
//
//            NodeList nodeList = doc.getElementsByTagName("item");
//
//            for (int i = 0; i < nodeList.getLength(); i++) {
//                Node node = nodeList.item(i);
//                if (node.getNodeType() == Node.ELEMENT_NODE) {
//                    Element element = (Element) node;
//
//                    String name = getElementContent(element, "facltNm");
//                    String introduction = getElementContent(element, "lineIntro");
//                    String sidoName = getElementContent(element, "doNm");
//                    String gugunName = getElementContent(element, "sigunguNm");
//                    String detailAddress = getElementContent(element, "addr1");
//                    BigDecimal longitude = new BigDecimal(getElementContent(element, "mapX"));
//                    BigDecimal latitude = new BigDecimal(getElementContent(element, "mapY"));
//                    String telephone = getElementContent(element, "tel");
//                    String homepageUrl = getElementContent(element, "homepage");
//                    String imageUrl = getElementContent(element, "firstImageUrl");
//
//                    int sidoCode = getSidoCode(sidoName);
//                    int gugunCode = getGugunCode(sidoCode, gugunName);
//
////                    System.out.println("Parsed camping: " + name + ", sido: " + sidoName + "(" + sidoCode + "), gugun: " + gugunName + "(" + gugunCode + ")");
//
//                    Camping camping = Camping.builder()
//                            .name(name)
//                            .introduction(introduction)
//                            .sidoName(sidoName)
//                            .sidoCode(sidoCode)
//                            .gugunName(gugunName)
//                            .gugunCode(gugunCode)
//                            .detailAddress(detailAddress)
//                            .longitude(longitude)
//                            .latitude(latitude)
//                            .telephone(telephone)
//                            .homepageUrl(homepageUrl)
//                            .imageUrl(imageUrl)
//                            .build();
//
//                    campingList.add(camping);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return campingList;
//    }
//
//    private static String getElementContent(Element element, String tagName) {
//        NodeList nodeList = element.getElementsByTagName(tagName);
//        if (nodeList != null && nodeList.getLength() > 0) {
//            Node node = nodeList.item(0);
//            if (node != null) {
//                return node.getTextContent();
//            }
//        }
//        return "";
//    }
//
//    private static int getSidoCode(String sidoName) {
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        int sidoCode = 0;
//
//        try {
//            conn = db.getConnection();
//            String sql = "SELECT sido_code FROM sidos WHERE sido_name = ?";
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, sidoName);
//            rs = pstmt.executeQuery();
//
//            if (rs.next()) {
//                sidoCode = rs.getInt("sido_code");
//            } else {
////                System.out.println("No sido_code found for sido_name: " + sidoName);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            db.close(rs, pstmt, conn);
//        }
//
//        return sidoCode;
//    }
//
//    private static int getGugunCode(int sidoCode, String gugunName) {
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        int gugunCode = 0;
//
//        try {
//            conn = db.getConnection();
//            String sql = "SELECT gugun_code FROM guguns WHERE sido_code = ? AND gugun_name = ?";
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setInt(1, sidoCode);
//            pstmt.setString(2, gugunName);
//            rs = pstmt.executeQuery();
//
//            if (rs.next()) {
//                gugunCode = rs.getInt("gugun_code");
//            } else {
////            	System.out.println("No gugun_code found for sido_code: " + sidoCode + " and gugun_name: " + gugunName);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            db.close(rs, pstmt, conn);
//        }
//
//        return gugunCode;
//    }
//
//    private static boolean isCampingExists(Connection conn, String campingName) throws SQLException {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        boolean exists = false;
//
//        try {
//            String sql = "SELECT COUNT(*) FROM camping WHERE name = ?";
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, campingName);
//            rs = pstmt.executeQuery();
//
//            if (rs.next()) {
//                exists = rs.getInt(1) > 0;
//            }
//        } finally {
//            db.close(rs, pstmt);
//        }
//
//        return exists;
//    }
}