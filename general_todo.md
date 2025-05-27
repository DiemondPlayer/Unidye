## TODOs and FIXMEs

### Gameplay:
- **TODO** check when the recipe component is messing with the itemstacks not being able to stack
- **TODO** native usage of recipe saving system
- **TODO** finish the naming system (add exclusions, prefixes, etc.) 
- **TODO** add Llama carpets
- **TODO** add Glazed Terracotta
- **FIXME** middlemouse+ctrl does not pick unidye blocks like intended
- **FIXME** dyed map markers; check MapIcon.Type, FilledMapItem, map_icons, MapRenderer
- **unlikely TODO** add jeb_ dye

### Code:
- **TODO** recipe stacks component being saved in block entities
- **TODO** create a system for two-layered dyeable blocks that support translucent textures
- **TODO** create a system for special resource-pack assets (for dyes, llama carpets, glazed terracotta, etc.)
- **TODO** merge leathery stuff with regular dyeable block entity stuff, making this more versatile
- **TODO** add a component for custom dye item so you won't be using CUSTOM_DATA
- **TODO** add/adapt a component for non-dye items to use
- **TODO** name saving database saving just the main dye color
- **consider TODO** component for saved name prefix
- **TODO** use accessors when possible

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