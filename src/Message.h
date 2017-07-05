#ifndef BLOCKCHAIN_MESSAGE_H
#define BLOCKCHAIN_MESSAGE_H

#include "header.h"

struct MessageToSend
{
    Message message;
    int sender_id;
    std::vector <int> target_id;
};

class Message
{
private:
    std::string type;
public:
    std::string get_message_type();
};


#endif //BLOCKCHAIN_MESSAGE_H
