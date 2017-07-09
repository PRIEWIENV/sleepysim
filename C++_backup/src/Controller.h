//
// Created by 谢天成 on 2017/7/5.
//

#ifndef SLEEPYSIM_CONTROLLER_H
#define SLEEPYSIM_CONTROLLER_H
#include "Framework.h"
#include "Signature.h"

class Controller {
private:
    Framework framework;
    Signature signature;
public:
    Controller();
    ~Controller();
    void one_round();
    bool detect_inconsistency();
    void print_log();
};


#endif //SLEEPYSIM_CONTROLLER_H
