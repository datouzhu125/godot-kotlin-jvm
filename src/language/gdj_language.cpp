#include "gdj_language.h"

#include "gd_kotlin.h"
#include "lifecycle/paths.h"
#include "names.h"
#include "script/jvm_script_manager.h"
#include "script/language/gdj_script.h"

constexpr const char* GDJ_TEMPLATE = "// GENERATED BY THE GODOT EDITOR. DON'T FORGET TO CREATE A CORRESPONDING SOURCE "
                                     "FILE! OTHERWISE THIS FILE WILL BE DELETED UPON NEXT BUILD.\n"
                                     "\n"
                                     "registeredName = " CLASS_TEMPLATE "\n"
                                     "fqName = " GODOT_KOTLIN_PACKAGE "." CLASS_TEMPLATE "\n"
                                     "relativeSourcePath = \"\"\n"
                                     "baseType = " BASE_TEMPLATE "\n"
                                     "supertypes = [\n"
                                     "    godot." BASE_TEMPLATE "\n"
                                     "]\n"
                                     "signals = []\n"
                                     "properties = []\n"
                                     "functions = []\n";

GdjLanguage* GdjLanguage::get_instance() {
    static GdjLanguage* instance {memnew(GdjLanguage)};
    return instance;
}

void GdjLanguage::init() {
    if (GDKotlin::get_instance().get_state() == GDKotlin::State::CORE_LIBRARY_INITIALIZED) {
        GDKotlin::get_instance().load_user_code();
    }

#ifdef DEBUG_ENABLED
    GDKotlin::get_instance().validate_state();
#endif
}

void GdjLanguage::thread_enter() {
    jni::Jvm::attach();
}

void GdjLanguage::thread_exit() {
    jni::Jvm::detach();
}

String GdjLanguage::get_name() const {
    return GODOT_JVM_LANGUAGE_NAME;
}

String GdjLanguage::get_type() const {
    return GODOT_JVM_SCRIPT_NAME;
}

String GdjLanguage::get_extension() const {
    return GODOT_JVM_REGISTRATION_FILE_EXTENSION;
}

void GdjLanguage::get_recognized_extensions(List<String>* p_extensions) const {
    p_extensions->push_back(GODOT_JVM_REGISTRATION_FILE_EXTENSION);
}

bool GdjLanguage::handles_global_class_type(const String& p_type) const {
    return p_type == GODOT_JVM_SCRIPT_NAME;
}

bool GdjLanguage::has_named_classes() const {
    return true;
}

bool GdjLanguage::supports_builtin_mode() const {
    return false;
}

Script* GdjLanguage::create_script() const {
    return memnew(GdjScript);
}

String GdjLanguage::get_global_class_name(const String& p_path, String* r_base_type, String* r_icon_path) const {
    if (p_path.begins_with(ENTRY_DIRECTORY) || !p_path.ends_with(GODOT_JVM_REGISTRATION_FILE_EXTENSION)) { return {}; }

    String script_name = JvmScript::get_script_file_name(p_path);
    Ref<NamedScript> named_script = JvmScriptManager::get_instance().get_script_from_name(script_name);
    if (!named_script.is_null() && named_script.is_valid()) {
        if (r_base_type) {
            if (named_script->get_base_script().is_null()) {
                *r_base_type = named_script->get_instance_base_type();
            } else {
                *r_base_type = named_script->get_base_script()->get_global_name();
            }
        }
        return named_script->get_global_name();
    }

    return {};
}

void GdjLanguage::get_reserved_words(List<String>* p_words) const {
    static const char* _reserved_words[] = {// RESERVED KEYWORDS
                                            "registeredName",
                                            "fqName",
                                            "relativeSourcePath ",
                                            "baseType ",
                                            "supertypes",
                                            "signals",
                                            "properties",
                                            "functions",
                                            nullptr
    };
    const char** w = _reserved_words;
    while (*w) {
        p_words->push_back(*w);
        w++;
    }
}

bool GdjLanguage::is_control_flow_keyword(String p_keyword) const {
    return false;
}

void GdjLanguage::get_comment_delimiters(List<String>* p_delimiters) const {
    p_delimiters->push_back("//");
}

void GdjLanguage::get_doc_comment_delimiters(List<String>* p_delimiters) const {}

void GdjLanguage::get_string_delimiters(List<String>* p_delimiters) const {}

Ref<Script> GdjLanguage::make_template(const String& p_template, const String& p_class_name, const String& p_base_class_name) const {
    Ref<GdjScript> gdj_script;
    gdj_script.instantiate();
    String processed_template {
      String(GDJ_TEMPLATE).replace(BASE_TEMPLATE, p_base_class_name).replace(CLASS_TEMPLATE, p_class_name.to_pascal_case())
    };
    gdj_script->set_source_code(processed_template);
    gdj_script->set_name(p_class_name);
    return gdj_script;
}

Vector<ScriptLanguage::ScriptTemplate> GdjLanguage::get_built_in_templates(StringName p_object) {
    return {};
}

bool GdjLanguage::is_using_templates() {
    return false;
}