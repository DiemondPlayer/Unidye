## TODOs and FIXMEs

### Gameplay:
- **TODO** native usage of recipe saving system
- **TODO** add Llama carpets
- **TODO** add Glazed Terracotta

### Code:
- **consider TODO** resourcepack driven stuff for everything else
- **TODO** move recipe stacks to be a database not a component
- **TODO** new crafting station?????
- **TODO** whole 'nother dye shape system (choose in the crafting station)
- **TODO** a datapack (?) system to add new "vanilla dyes" without new items

### Config:
- **TODO** add "no added spaces option"
- **TODO** create config
- **TODO** add perm levels of commands to config
- **TODO** add ownership system to config (can only the author edit the name?) (discovery system)

### Assets:
- **TODO** new dye sprites (for lime, pink, gray, purple, magenta) and new unique shapes
- **FIXME** item models are weird with carpets and wool

### Compat (Main Mod):
- **TODO** built-in datapack system for recipe, advancements, etc.
- **TODO** use the former mentioned datapack system to add new recipe advancements when other dye mods are present
- **FIXME** EMI recipe tree knows no diff between custom dyes
- **FIXME** REI compat gives way too many example displays
- **consider TODO** replace fillers with just 1 display per recipe in REI compat

### Misc:
- **TODO** final world conversion check (block entities still broken)
- **TODO** check every class for warns, errors
- **TODO** cleanup/add comments
- **TODO** add warns/errors through LOGGER where it is needed

### The Unlikely Corner:
- **unlikely TODO** naming system for fireworks
- **unlikely TODO** updating naming system for fireworks
- **unlikely FIXME** dyed map markers; check MapIcon.Type, FilledMapItem, map_icons, MapRenderer
- **unlikely TODO** add jeb_ dye
- **unlikely TODO** remove ItemEntityMixin.class
- **unlikely TODO** JEI proper compat
- **unlikely TODO** JEI recipe saving impl
- **unlikely TODO** figure out whether issuing a sync additionally is worth it
- **unlikely TODO** second layer concrete powder