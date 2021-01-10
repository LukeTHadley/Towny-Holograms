# Towny Holograms
###### A Spigot Minecraft plugin that gives town mayors/members access to create and manage holograms specifically inside their town.

---

## About the Plugin

This plugin adds the ability for town mayors/workers to create and manage holograms, restricting the holograms to only being inside their town blocks.

The cost of each hologram can be modified in the `config.yml` as well as how many holograms a town can have depending on the size of the town (number of residents) giving server admins full control over how many holograms towns can have.
(Perhaps add an ability to tax depending on how many holograms on towny-day)

Holograms are removed and deleted when a town falls.
Holograms can be cleared in an individual plot on `/plot clear` towny event.
Holograms are automatically cleared


### Dependencies

This plugin requires:
    - Towny
    - Holographic Displays

![Hologram](https://imgur.com/ldZ63VQ.png)

![Prices](https://imgur.com/LvYDQNH.png)

---

## Commands and Permissions

Permissions are made for each command so that server admins can easily pick and choose what town ranks have access to each command.

For example, `Mayor` would have access to all commands. `Assistant` has access to modify the content of an existing hologram. `Builder` has the ability to modify and move an existing hologram.

(Can't get wildcard permissions working at the moment :ANGER:)

#### General Permissions

| Command                                              | Description                                | Permission                    |
| -----------------------------------------------------|:------------------------------------------:| -----------------------------:|
| `/townyhologram`                                     | Shows plugin information                   |                               |
| `/townyhologram create <hologram name> [text]`       | Make a new hologram in your town           | `townyholograms.commands.create` |
| `/townyhologram delete <hologram name>`              | Delete a hologram in your town             | `townyholograms.commands.delete` |
| `/townyhologram list`                                | Show a list of all holograms in your town  | `townyholograms.commands.list`   |
| `/townyhologram info <hologram name> `               | Get information on a specific hologram in your town | `townyholograms.commands.create` |
| `/townyhologram addline <hologram name> [text]`      | Add a line to a given hologram             | `townyholograms.commands.addline`|
| `/townyhologram removeline <hologram name> <index>`  | Remove a line from a given hologram        | `townyholograms.commands.removeline` |
| `/townyhologram setline <hologram name> <index> [text]` | Set the content of an existing line     | `townyholograms.commands.setline` |
| `/townyhologram insertline <hologram name> <index> [text]` | Insert a line into an existing hologram           | `townyholograms.commands.insertline` |
| `/townyhologram move <hologram name> here/<X Position> <Y Position> <Z Position>` | Move a hologram to a given position in the town | `townyholograms.commands.move` |
| `/townyhologram prices`                                | Shows hologram prices and settings of the plugin         | `townyholograms.commands.prices` |
| `/townyhologram help`                                | Show a list of all plugin commands         | `townyholograms.commands.help` |



#### Admin Permissions (TODO)

Admins have separate permission nodes to be able to modify other towns holograms.

| Command                                              | Description                                | Permission                    |
| -----------------------------------------------------|:------------------------------------------:| -----------------------------:|
| `/townyhologram admin <>`                            | X                                          | `townyhologram.admin.`        |


---


## Configuration

```yaml
##########################
# TownyHolograms
# @author Thomas9078
# Config File
##########################


#Add your SQL connection credentials below.
#The plugin will not enable unless a successful SQL connection is found
sql-database:
  host: ""
  port: 3306
  database-name: ""
  username: ""
  password: ""

# Should holograms in a plot be cleared when a user runs '/plot clear'
ClearHologramsOnPlotClear: false

# The price of a new hologram taken out of the town bank
NewHologramPrice: 10000

# The number of residents to the number of holograms allowed.
# [1, 2] - If there is one resident in the town, the town is allowed 2 holograms
# [2, 7] - If there are 2 residents in the town, the town is allowed 7 holograms...
# Add more in the format below depending on your server.
hologram-allowances:
  - [1, 2]
  - [2, 3]
  - [10, 15]
```

## Versions & Footnotes

This project was created based on [Spigot-1.16.4 Server](https://getbukkit.org/get/MvbtKzCMFRVUPyKHvZ0cmiThXiiTSe92) using [Holographic Displays X](X) and [Towny X](X)




I have no affiliation with the creators of Towny or the creators of Holographic Displays


## ToDo

A line limit

Price to add a line to a hologram

Hologram Tax per townyday
