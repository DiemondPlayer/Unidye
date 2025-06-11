## TODOs and FIXMEs

### Gameplay:
- **TODO** native usage of recipe saving system
- **TODO** add Llama carpets
- **TODO** add Glazed Terracotta

### Code:
- **FIXME** shield + banner go no no worky
- **TODO** add a capitalization version when there are no prefixes and only suffix
- **TODO** add ownership system
- **TODO** a system to add new "vanilla dyes" without new items (aka fix your awful dye shape code again)
- **TODO** merge leathery stuff with regular dyeable block entity stuff, making this more versatile
- **TODO** add a component for custom dye item so you won't be using CUSTOM_DATA
- **TODO** add/adapt a component for non-dye items to use

### Config:
- **TODO** add "no added spaces option"
- **TODO** create config
- **TODO** add perm levels of commands to config
- **TODO** add ownership system to config (can only the author edit the name?) (discovery system)

### Assets:
- **TODO** if the "two-layered dyeable blocks" system is implemented, utilise it
- **TODO** new dye sprites for lime, pink, gray, purple, magenta

### Compat (Main Mod):
- **TODO** built-in datapack system for recipe, advancements, etc.
- **TODO** use the former mentioned datapack system to add new shapes when other dye mods are present
- **FIXME** EMI recipe tree knows no diff between custom dyes
- **FIXME** REI compat gives way too many example displays
- **consider TODO** replace fillers with just 1 display per recipe in REI compat

### Misc:
- **TODO** final world conversion check
- **TODO** check every class for warns, errors
- **TODO** cleanup/add comments
- **TODO** add warns/errors through LOGGER where it is needed
- **TODO** organize command names (because right now they are not intuitive probs)

### The Unlikely Corner:
- **unlikely TODO** naming system for fireworks
- **unlikely TODO** updating naming system for fireworks
- **unlikely FIXME** dyed map markers; check MapIcon.Type, FilledMapItem, map_icons, MapRenderer
- **unlikely TODO** add jeb_ dye
- **unlikely TODO** remove ItemEntityMixin.class
- **unlikely TODO** JEI proper compat
- **unlikely TODO** JEI recipe saving impl
- **unlikely TODO** figure out whether issuing a sync additionally is worth it