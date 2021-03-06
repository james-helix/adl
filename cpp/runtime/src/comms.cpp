#include <adl/comms.h>

namespace ADL {
namespace comms {

void
ConnectionFactory::registerTransport( const TransportName &tname, RawConnectionFactory::Ptr rc )
{
    if( transportFactories_.find(tname) != transportFactories_.end() )
        assert( false ); // Illegal re-registration of transport

    transportFactories_[tname] = rc;
}

Connection<RawBufferPtr>::Ptr
ConnectionFactory::connect0( std::shared_ptr<ADL::sys::sinkimpl::SinkData> sdata )
{
    // Locate the transport
    auto mi = transportFactories_.find( sdata->transport );
    if( mi == transportFactories_.end() )
        assert( false );  // No registered transport found
    RawConnectionFactory::Ptr t = mi->second;

    // Make a connection
    return t->connect( sdata->address );
}

const ADL::SerialiserFlags commsSerialiserFlags = ADL::SerialiserFlags();

};
};
