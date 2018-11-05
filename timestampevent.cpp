#include "timestampevent.h"

TimestampEvent::TimestampEvent(long timestamp) :
    ITMEvent(),
    m_timestamp(timestamp)
{

}

long TimestampEvent::timestamp() const
{
    return m_timestamp;
}
