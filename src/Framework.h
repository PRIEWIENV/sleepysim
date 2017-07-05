#ifndef BLOCKCHAIN_FRAMEWORK_H
#define BLOCKCHAIN_FRAMEWORK_H

#include "header.h"
#include "Signature.h"
#include "Node.h"
#include "Message.h"

class Framework
{
private:
    std::vector <Node> node_list;
    std::vector <MessageToSend> message_send_buffer;
public:
    Framework();
    ~Framework();
    void send_message(Message message, std::vector <int> target);
};


#endif //BLOCKCHAIN_FRAMEWORK_H
