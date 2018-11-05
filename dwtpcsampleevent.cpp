#include "dwtpcsampleevent.h"

DWTPCSampleEvent::DWTPCSampleEvent(quint8 type_, long pcAddress) :
    ITMEvent(),
    m_type(type_),
    m_pcAddress(pcAddress)
{

}

long DWTPCSampleEvent::pcAddress() const
{
    return m_pcAddress;
}
