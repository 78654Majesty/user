package com.security.demo.service.impl;

import com.security.demo.entity.ProcessParamsContext;
import com.security.demo.provider.WebApiAdaptorProvider;
import com.security.demo.provider.WebApiValidatorProvider;
import com.security.demo.service.WebApiTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author fanglingxiao
 * @desc
 * @date 2019/12/11
 */
@Service
public class WebApiTestServiceImpl implements WebApiTestService {

    @Autowired
    private WebApiValidatorProvider webApiValidatorProvider;

    @Autowired
    private WebApiAdaptorProvider webApiAdaptorProvider;

    @Override
    public String process(ProcessParamsContext context) {
        Function<ProcessParamsContext,Boolean> validator = webApiValidatorProvider.validator(context);
        Consumer<ProcessParamsContext> adaptor = webApiAdaptorProvider.adaptor(context);
        // 调用Function函数执行结果
        if (!validator.apply(context)){
            return "error";
        }
        // 调用Consumer函数执行结果
        adaptor.accept(context);
        // todo 执行入库操作
        // todo 返回调用者结果
        return null;
    }
}
