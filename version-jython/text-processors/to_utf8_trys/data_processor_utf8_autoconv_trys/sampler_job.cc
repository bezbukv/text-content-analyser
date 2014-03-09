// encoding : utf8
// "Copyright [year] <Copyright Owner>"  [legal/copyright]
#include <app-server-code/data_processor/sampler_uni_header.h>

namespace tmitter_web_service {
//} // namespace tmitter_web_service

using std::string;
//using std::v ector;
using std::size_t;
using std::copy;
using std::back_inserter;
using std::map;
using std::pair;

//using ::stdext::hash_map;

using simple_type_processors::int2str;
using simple_type_processors::hl;
using simple_type_processors::uint8;

// TODO_N: лишние вызовы в релизе
void Sampler::InserArrayPart(
     const uint8* ptr, 
     const int length,
     const string marker) {

#ifdef DATA_SCANER
  v ector<uint8> result;

  copy(ptr, ptr+length, back_inserter(result));

  // печатаем в лог
  uint8* tmp_ptr = &result.front();
  LOG_ARRAY(0, length, tmp_ptr, 0, marker)

  mapData_.push_back(result);
#endif
}

} // namespace tmitter_web_service