local keyboard = require("keyboard")

keyboard.keys["1"]           = 0x31
keyboard.keys["2"]           = 0x32
keyboard.keys["3"]           = 0x33
keyboard.keys["4"]           = 0x34
keyboard.keys["5"]           = 0x35
keyboard.keys["6"]           = 0x36
keyboard.keys["7"]           = 0x37
keyboard.keys["8"]           = 0x38
keyboard.keys["9"]           = 0x39
keyboard.keys["0"]           = 0x30
keyboard.keys.a               = 0x41
keyboard.keys.b               = 0x42
keyboard.keys.c               = 0x43
keyboard.keys.d               = 0x44
keyboard.keys.e               = 0x45
keyboard.keys.f               = 0x46
keyboard.keys.g               = 0x47
keyboard.keys.h               = 0x48
keyboard.keys.i               = 0x49
keyboard.keys.j               = 0x4A
keyboard.keys.k               = 0x4B
keyboard.keys.l               = 0x4C
keyboard.keys.m               = 0x4D
keyboard.keys.n               = 0x4E
keyboard.keys.o               = 0x4F
keyboard.keys.p               = 0x50
keyboard.keys.q               = 0x51
keyboard.keys.r               = 0x52
keyboard.keys.s               = 0x53
keyboard.keys.t               = 0x54
keyboard.keys.u               = 0x55
keyboard.keys.v               = 0x56
keyboard.keys.w               = 0x57
keyboard.keys.x               = 0x58
keyboard.keys.y               = 0x59
keyboard.keys.z               = 0x5A

-- TODO
--keyboard.keys.apostrophe      = 0x28
--keyboard.keys.at              = 0x91
keyboard.keys.back            = 0x08 -- backspace
--keyboard.keys.backslash       = 0x2B
--keyboard.keys.capital         = 0x3A -- capslock
--keyboard.keys.colon           = 0x92
--keyboard.keys.comma           = 0x33
--keyboard.keys.enter           = 0x0A
--keyboard.keys.equals          = 0x0D
--keyboard.keys.grave           = 0x29 -- accent grave
--keyboard.keys.lbracket        = 0x1A
--keyboard.keys.lcontrol        = 0x1D
--keyboard.keys.lmenu           = 0x38 -- left Alt
--keyboard.keys.lshift          = 0x2A
--keyboard.keys.minus           = 0x0C
--keyboard.keys.numlock         = 0x45
--keyboard.keys.pause           = 0xC5
--keyboard.keys.period          = 0x34
--keyboard.keys.rbracket        = 0x1B
--keyboard.keys.rcontrol        = 0x9D
--keyboard.keys.rmenu           = 0xB8 -- right Alt
--keyboard.keys.rshift          = 0x36
--keyboard.keys.scroll          = 0x46 -- Scroll Lock
--keyboard.keys.semicolon       = 0x27
--keyboard.keys.slash           = 0x35 -- / on main keyboard
keyboard.keys.space           = 0x20
--keyboard.keys.stop            = 0x95
--keyboard.keys.tab             = 0x0F
--keyboard.keys.underline       = 0x93

-- Keypad (and numpad with numlock off)
keyboard.keys.up              = 0xC8
keyboard.keys.down            = 0xD0
keyboard.keys.left            = 0xCB
keyboard.keys.right           = 0xCD
keyboard.keys.home            = 0xC7
keyboard.keys["end"]         = 0xCF
keyboard.keys.pageUp          = 0xC9
keyboard.keys.pageDown        = 0xD1
keyboard.keys.insert          = 0xD2
keyboard.keys.delete          = 0xD3

-- Function keys
keyboard.keys.f1              = 0x70
keyboard.keys.f2              = 0x71
keyboard.keys.f3              = 0x72
keyboard.keys.f4              = 0x73
keyboard.keys.f5              = 0x74
keyboard.keys.f6              = 0x75
keyboard.keys.f7              = 0x76
keyboard.keys.f8              = 0x77
keyboard.keys.f9              = 0x78
keyboard.keys.f10             = 0x79
keyboard.keys.f11             = 0x7A
keyboard.keys.f12             = 0x7B
keyboard.keys.f13             = 0x7C
keyboard.keys.f14             = 0x7D
keyboard.keys.f15             = 0x7E
keyboard.keys.f16             = 0x7F
keyboard.keys.f17             = 0x80
keyboard.keys.f18             = 0x81
keyboard.keys.f19             = 0x82

-- Japanese keyboards
keyboard.keys.kana            = 0x70
keyboard.keys.kanji           = 0x94
keyboard.keys.convert         = 0x79
keyboard.keys.noconvert       = 0x7B
keyboard.keys.yen             = 0x7D
keyboard.keys.circumflex      = 0x90
keyboard.keys.ax              = 0x96

-- Numpad
keyboard.keys.numpad0         = 0x60
keyboard.keys.numpad1         = 0x61
keyboard.keys.numpad2         = 0x62
keyboard.keys.numpad3         = 0x63
keyboard.keys.numpad4         = 0x64
keyboard.keys.numpad5         = 0x65
keyboard.keys.numpad6         = 0x66
keyboard.keys.numpad7         = 0x67
keyboard.keys.numpad8         = 0x68
keyboard.keys.numpad9         = 0x69
keyboard.keys.numpadmul       = 0x6A
keyboard.keys.numpaddiv       = 0x6F
keyboard.keys.numpadsub       = 0x6D
keyboard.keys.numpadadd       = 0x6B
keyboard.keys.numpaddecimal   = 0x53
keyboard.keys.numpadenter     = 0x0A

-- Create inverse mapping for name lookup.
setmetatable(keyboard.keys,
{
  __index = function(tbl, k)
    if type(k) ~= "number" then return end
    for name,value in pairs(tbl) do
      if value == k then
        return name
      end
    end
  end
})
