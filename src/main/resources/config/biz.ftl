<#-- Sample class template -->
package ${package};

import com.alibaba.fastjson.JSONObject;
import com.engine.common.util.ParamUtil;
import com.engine.common.util.ServiceUtil;
import com.engine.${path}.service.${className}Service;
import com.engine.${path}.service.impl.${className}ServiceImpl;
import com.weaver.formmodel.mobile.manager.MobileUserInit;
import weaver.hrm.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.util.HashMap;
import java.util.Map;

public class ${className}Bizz {

    private ${className}Service getService() {
        return (${className}Service) ServiceUtil.getService(${className}ServiceImpl.class);
    }
    <#list lists as li>
    @POST
    @Path("${li.path}")
    public String post${li.random}(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        Map<String, Object> apidatas = new HashMap<String, Object>();
        User user = MobileUserInit.getUser(request,response);
        try {
            apidatas.putAll(getService().generate${li.random}(ParamUtil.request2Map(request),user));
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    @GET
    @Path("${li.path}")
    public String get${li.random}(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        Map<String, Object> apidatas = new HashMap<String, Object>();
        User user = MobileUserInit.getUser(request,response);
        try {
            apidatas.putAll(getService().generate${li.random}(ParamUtil.request2Map(request),user));
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }
    </#list>
}
