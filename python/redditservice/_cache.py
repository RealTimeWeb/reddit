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
    Internal method that empties the local cache.
    :returns: void
    """
    CACHE= {}
def load():
    """
    Internal method that loads the local cache.
    :returns: void
    """
    CACHE = _recursively_convert_unicode_to_str(json.load(open(cache.json, r)))
def lookup(key):
    """
    Internal method that looks up a key in the local cache.
    :param key: Get the value based on the key from the cache.
    :type key: string
    :returns: void
    """
    return CACHE.get(key, "")
