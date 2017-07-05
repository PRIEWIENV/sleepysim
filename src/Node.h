#ifndef BLOCKCHAIN_NODE_H
#define BLOCKCHAIN_NODE_H

#include "header.h"
#include "Message.h"
#include "Signature.h"

class Node
{
private:
    std::vector <Message> message_buffer;
    int node_id;
public:
    Node();
    ~Node();
    std::vector <Message> received_message_buffer;
    std::vector <MessageToSend> sent_message_buffer;
    void send_message(Message message, std::vector <int> target);
    void receive_message();
    std::string request_signature(Message message);
    bool check_signature(int node_id, std::string signature);
};


#endif //BLOCKCHAIN_NODE_H
