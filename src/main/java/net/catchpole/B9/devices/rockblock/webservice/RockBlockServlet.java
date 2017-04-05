package net.catchpole.B9.devices.rockblock.webservice;

import net.catchpole.B9.lang.HexString;
import net.catchpole.B9.lang.Throw;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*

Example web.xml

<?xml version="1.0" encoding="ISO-8859-1" ?>
<web-app xmlns="http://java.sun.com/xml/ns/j2ee"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd"
    version="2.4">

    <display-name>RockBlock Application</display-name>

    <servlet>
        <servlet-name>RockBlockServlet</servlet-name>
        <servlet-class>net.catchpole.B9.devices.rockblock.webservice.RockBlockServlet</servlet-class>
        <init-param>
            <param-name>messageHandlerClass</param-name>
            <param-value>net.catchpole.B9.devices.rockblock.webservice.MOMessagePrinter</param-value>
        </init-param>
    </servlet>

    <servlet-mapping>
        <servlet-name>RockBlockServlet</servlet-name>
        <url-pattern>/putYourSecretEndpointHere</url-pattern>
    </servlet-mapping>
</web-app>

*/

public class RockBlockServlet extends HttpServlet {
    private MOMessageHandler moMessageHandler;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        try {
            moMessageHandler = (MOMessageHandler)Class.forName(servletConfig.getInitParameter("messageHandlerClass")).newInstance();
        } catch (Exception e) {
            Throw.unchecked(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        MOMessage moMessage = new MOMessage();
        moMessage.setImei(httpServletRequest.getParameter("imei"));
        moMessage.setMomsn(httpServletRequest.getParameter("momsn"));
        moMessage.setTransmitTime(httpServletRequest.getParameter("transmit_time"));
        moMessage.setIridiumLatitude(httpServletRequest.getParameter("iridium_latitude"));
        moMessage.setIridiumLongitude(httpServletRequest.getParameter("iridium_longitude"));
        moMessage.setIridiumCep(httpServletRequest.getParameter("iridium_cep"));
        moMessage.setData(HexString.toByteArray(httpServletRequest.getParameter("data")));
        this.moMessageHandler.handle(moMessage);
    }
}