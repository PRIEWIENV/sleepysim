#include "Message.h"

bool operator < (const MessageToSend &a, const MessageToSend &b)
{
    return a.send_time < b.send_time;
}

std::string Message::get_message_type()
{
    return this->type;
}