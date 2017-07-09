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
    std::set <MessageToSend> message_send_buffer;
    Signature* signature;
public:
    Framework(int node_count, int adversary_count, Signature* signature);
    ~Framework();
    void send_message(Message message, std::vector <int> target);
    void receive_message();
    void run(int current_round);
    void accept_leader(std::vector <int> leader_list);
    // run a round, send the messages received in the last round to each node, wait for nodes' run,
    // and then receive the messages from each node.
};


#endif //BLOCKCHAIN_FRAMEWORK_H
