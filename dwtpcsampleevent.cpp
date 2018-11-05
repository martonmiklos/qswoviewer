#include "dwtpcsampleevent.h"

DWTPCSampleEvent::DWTPCSampleEvent(byte type_, long pcAddress) :
    ITMEvent(),
    m_type(type_),
    m_pcAddress(pcAddress)
{

}

long DWTPCSampleEvent::pcAddress() const
{
    return m_pcAddress;
}
