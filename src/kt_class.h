#ifndef GODOT_JVM_KTCLASS_H
#define GODOT_JVM_KTCLASS_H

#include "jni/jvm.h"
#include "core/reference.h"
#include "java_instance_wrapper.h"
#include "kt_object.h"

class KtClass : public JavaInstanceWrapper {
public:
    String name;
    String super_class;

    KtClass(jni::JObject p_wrapped, jni::JObject& p_class_loader);
    ~KtClass() = default;

    KtObject* create_instance(jni::Env& env, const Variant** p_args, int p_arg_count, Object* p_owner);

private:
    String get_name(jni::Env& env);
    String get_super_class(jni::Env& env);
};

#endif //GODOT_JVM_KTCLASS_H