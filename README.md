#Towny Holograms
######A Spigot Minecraft plugin that gives town mayors/members access to create and manage holograms specifically inside their town.

---

##About the Plugin



This plugin requires:
    Towny
    Holographic Displays

---

##Commands and Permissions

Permissions are made for each command so that server admins can easily pick and choose what town ranks have access to each permissions.

####General Permissions

| Command                                              | Description                                | Permission                    |
| -----------------------------------------------------|:------------------------------------------:| -----------------------------:|
| `/townyhologram`                                     | Shows plugin information                   |                               |
| `/townyhologram create <hologram name> [text]`       | Make a new hologram in your town           | `townyhologram.commands.create` |
| `/townyhologram delete <hologram name>`              | Delete a hologram in your town             | `townyhologram.commands.delete` |
| `/townyhologram list`                                | Show a list of all holograms in your town  | `townyhologram.commands.list`   |
| `/townyhologram info <hologram name> `               | Get information on a specific hologram in your town | `townyhologram.commands.create` |
| `/townyhologram addline <hologram name> [text]`      | Add a line to a given hologram             | `townyhologram.commands.addline`|
| `/townyhologram removeline <hologram name> <index>`  | Remove a line from a given hologram        | `townyhologram.commands.removeline` |
| `/townyhologram setline <hologram name> <index> [text]` | Set the content of an existing line     | `townyhologram.commands.setline` |
| `/townyhologram insertline <hologram name> <index> [text]` | Insert a line into an existing hologram           | `townyhologram.commands.insertline` |
| `/townyhologram move <hologram name> here|<X Position> <Y Position> <Z Position>` | Move a hologram to a given position in the town | `townyhologram.commands.move` |
| `/townyhologram help`                                | Show a list of all plugin commands         | `townyhologram.commands.help` |



####Admin Permissions

Admins have seperate permission nodes to be able to modify other towns holograms.

| Command                                              | Description                                | Permission                    |
| -----------------------------------------------------|:------------------------------------------:| -----------------------------:|
| `/townyhologram admin <>`                            | X                                          | `townyhologram.admin.`        |


---

##Versions & Footnotes

This project was created based on [Spigot-1.16.4 Server](https://getbukkit.org/get/MvbtKzCMFRVUPyKHvZ0cmiThXiiTSe92) using [Holographic Displays X](X) and [Towny X](X)



I have no affiliation with the creators of Towny or the creators of Holographic Displays
