## TODOs and FIXMEs

### Gameplay:
- **FIXME** shearing a sheep gives a wool with no prefix component
- **TODO** native usage of recipe saving system
- **TODO** add exclusions to naming system
- **TODO** recipe stacks should remember tags when possible
- **unlikely TODO** naming system for fireworks
- **unlikely TODO** updating naming system for fireworks
- **TODO** add Llama carpets
- **TODO** add Glazed Terracotta
- **unlikely FIXME** dyed map markers; check MapIcon.Type, FilledMapItem, map_icons, MapRenderer
- **unlikely TODO** add jeb_ dye

### Code:
- **TODO** add "no added spaces option"
- **TODO** add a capitalization version when there are no prefixes and only suffix
- **TODO** figure out whether issuing a sync additionally is worth it
- **TODO** add ownership system
- **TODO** create config
- **TODO** add perm levels of commands to config
- **TODO** add ownership system to config (can only the author edit the name?) (discovery system)
- **TODO** create a system for two-layered dyeable blocks that support translucent textures
- **TODO** create a system for special resource-pack assets (for dyes, llama carpets, glazed terracotta, etc.)
- **TODO** merge leathery stuff with regular dyeable block entity stuff, making this more versatile
- **TODO** add a component for custom dye item so you won't be using CUSTOM_DATA
- **TODO** add/adapt a component for non-dye items to use
- **consider TODO** remove ItemEntityMixin.class
- **TODO** organize command names (because right now they are not intuitive probs)

### Assets:
- **TODO** if the "two-layered dyeable blocks" system is implemented, utilise it

### Compat (Main Mod):
- **TODO** built-in datapack system for recipe, advancements, etc.
- **TODO** use the former mentioned datapack system to add new shapes when other dye mods are present
- **TODO** JEI proper compat
- **TODO** JEI recipe saving impl
- **TODO** EMI automatic cauldron washing recipes based on CauldronBehavior
- **FIXME** EMI disable cauldron washing recipes that aren't actually there
- **FIXME** EMI recipe tree knows no diff between custom dyes
- **FIXME** REI compat gives way too many example displays
- **consider TODO** replace fillers with just 1 display per recipe in REI compat

### Misc:
- **TODO** final world conversion check
- **TODO** check every class for warns, errors
- **TODO** cleanup/add comments
- **TODO** add warns/errors through LOGGER where it is needed