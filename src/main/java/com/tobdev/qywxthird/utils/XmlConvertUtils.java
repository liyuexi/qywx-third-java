package com.tobdev.qywxthird.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlConvertUtils {

    /**
     * xml转换成JavaBean
     *
     * @param xml xml格式字符串
     * @param t 待转化的对象
     * @return 转化后的对象
     * @throws Exception JAXBException
     */
    @SuppressWarnings({ "unchecked" })
    public static <T> T convertToJavaBean(String xml, Class<T> t) throws Exception {
        T obj = null;
        JAXBContext context = JAXBContext.newInstance(t);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        obj = (T) unmarshaller.unmarshal(new StringReader(xml));
        return obj;
    }

    /**
     * JavaBean转换成xml
     * @param obj
     * @param encoding
     * @return
     */
    public static String convertToXml(Object obj, String encoding) {
        String result = null;
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", false);
            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            result = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

//    public static JSONObject xmlToJson(String xml) {
//        try {
//            org.json.JSONObject object = XML.toJSONObject(xml);
//            String jsonData = object.get("response").toString();
//            JSONObject jsonObject = JSON.parseObject(jsonData);
//            return jsonObject;
//			/*JSONObject result = (JSONObject) jsonObject.get("result");
//			JSONObject upload = (JSONObject) result.get("upload");
//			String uploadUrl = upload.get("url").toString();
//			String fildId = upload.get("upload-file-id").toString();*/
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }


}
