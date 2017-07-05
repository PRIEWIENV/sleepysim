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
    Signature *signature;
public:
    Node(int node_id, Signature *signature);
    ~Node();
    void send_message();
    void receive_message(Message message);
    std::string request_signature(Message message);
    bool check_signature(int node_id, std::string signature);
    void run(int current_round); // for other group
};


#endif //BLOCKCHAIN_NODE_H
