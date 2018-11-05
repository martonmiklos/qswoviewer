#ifndef TIMESTAMPEVENT_H
#define TIMESTAMPEVENT_H

#include "itmevent.h"

class TimestampEvent : public ITMEvent
{
public:
    TimestampEvent(long timestamp);
    long timestamp() const;

private:
    long m_timestamp;
};

#endif // TIMESTAMPEVENT_H
