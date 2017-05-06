#include <jni.h>
#include <string>

extern "C"
jstring
Java_com_sorashiro_metroplanning_jni_CoreData_stringFromJNI(
        JNIEnv *env,
        jobject object) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
jstring
Java_com_sorashiro_metroplanning_jni_CoreData_getLevelData(
        JNIEnv *env, jobject object, jint level) {

    std::string result = "";

    switch (level) {
        case 1:
            result += "6 6 20 100,";
            result += "2 ";
            result += "0 0 black down 1000 50 0 4 black down 1000 50,";
            result += "2 ";
            result += "2 1 black 100 4 3 black 0,";
            result += "4 ";
            result += "1 0 black down 3 0 black right 3 4 black up 1 4 black left";
            break;
        case 2:
            result += "6 6 30 300,";
            result += "2 ";
            result += "2 2 black up 1000 50 4 2 black down 1000 50,";
            result += "3 ";
            result += "1 3 black 100 3 1 black 100 4 4 black 100,";
            result += "5 ";
            result += "1 0 black left 3 0 black down 1 2 black down 5 2 black right 5 4 black up";
            break;
        case 3:
            result += "6 6 30 300,";
            result += "2 ";
            result += "2 2 black up 1000 50 4 2 black down 1000 50,";
            result += "3 ";
            result += "1 3 black 100 3 1 black 100 4 4 black 100,";
            result += "5 ";
            result += "1 0 black left 3 0 black down 1 2 black down 5 2 black right 5 4 black up";
            break;

        default:
            break;
    }

    return env->NewStringUTF(result.c_str());
}

extern "C"
jint
Java_com_sorashiro_metroplanning_jni_CoreData_getLevels(
        JNIEnv *env, jobject object) {

    return 3;
}
