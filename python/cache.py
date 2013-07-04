import json

def _recursively_convert_unicode_to_str(input):
    if isinstance(input, dict):
        return {_recursively_convert_unicode_to_str(key): _recursively_convert_unicode_to_str(value) for key, value in input.iteritems()}
    elif isinstance(input, list):
        return [_recursively_convert_unicode_to_str(element) for element in input]
    elif isinstance(input, unicode):
        return input.encode('utf-8')
    else:
        return input

CACHE = {}
def unload():
	"""
	
	:returns:
	"""
	CACHE= {}
def load():
	"""
	
	:returns:
	"""
	CACHE = _recursively_convert_unicode_to_str(json.load(open(cache.json, r)))
def lookup(key):
	"""
	
	:param key: _
	:type key: _
	:returns:
	"""
	return CACHE.get(key, "")
