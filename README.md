# ${\color{lightblue} System \space Design \space Components}$

The components that are widely used in designing large scalable systems. Many Software Developers and System Architects use these components and their hybrid structures one way or the other.

## ${Folder \space Structure}$

| ${\color{red}No.}$ | ${\color{red}Component \space Name}$ | ${\color{red}Design \space Patterns}$ | ${\color{red}Explanation}$ | ${\color{red}Repo}$ | ${\color{red}Project \space used}$ |
|-|-|-|-|-|-|
| $${1.}$$ | ${\color{lightgreen} Composable \space Factory \space Tree}$ | $${Factory \space + \space Decorator}$$ | A tree structure bound by root where each node behaves like factory sharing some common properties of root node and deeper nodes need parent nodes as dependency to produce objects | [FolderLink](https://github.com/VishuKalier2003/Moderation-Pipeline) | *Moderation Pipeline* |
| $${2.}$$ | ${\color{lightgreen} Notification \space Generic \space Adapter}$ | ${Adapter \space + \space Strategy \space + \space Generics}$ | Generic adapter that takes input of type X (X1, X2, ... Xn) and converts into of type Y, like a multi-socket plug system | [FolderLink](https://github.com/VishuKalier2003/Notification-Adapter) | *Notification Adapter* |
