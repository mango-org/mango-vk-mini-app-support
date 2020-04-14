package com.mango.support.miniapp;

import com.mango.support.miniapp.classes.HMACSignVerifier;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class MangoVkMiniAppSupportExtension extends Extension {
    public static final String NS = "php\\vk\\miniapp";
    
    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }
    
    @Override
    public void onRegister(CompileScope scope) {
        registerClass(scope, HMACSignVerifier.class);
    }
}
