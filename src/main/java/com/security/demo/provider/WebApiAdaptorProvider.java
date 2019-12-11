package com.security.demo.provider;

import com.security.demo.entity.ProcessParamsContext;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * @author fanglingxiao
 * @desc 适配器
 * @date 2019/12/11
 */
@Component
public class WebApiAdaptorProvider {
    public Consumer<ProcessParamsContext> adaptor(ProcessParamsContext context) {
        String platform = context.getRequest().getParameter("platform");
        switch (platform) {
            case "DY":
                return this::dyAdaptor;
            case "PDD":
                return this::pddAdaptor;
            default:
                throw new UnsupportedOperationException("no support platform");
        }
    }

    private void dyAdaptor(ProcessParamsContext context){
        context.setRequestParam("");
    }

    private void pddAdaptor(ProcessParamsContext context){
        context.setRequestParam("");
    }
}
