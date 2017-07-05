#ifndef BLOCKCHAIN_PROTOCOL_H
#define BLOCKCHAIN_PROTOCOL_H

#include "header.h"
#include "Framework.h"

class Protocol
{
private:
    Framework framework;
    int current_round;
public:
    std::vector <int> elect_leader();
    bool detect_inconistency();
    void run();
};


#endif //BLOCKCHAIN_PROTOCOL_H
