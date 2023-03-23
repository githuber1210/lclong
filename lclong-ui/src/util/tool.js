export function getTextFormHtml(html) {
    return html.replace(/<(style|script|iframe)[^>]*?>[\s\S]+?<\/\1\s*>/gi, '').replace(/<[^>]+?>/g, '').replace(/\s+/g, ' ').replace(/ /g, ' ').replace(/>/g, ' ')
}

export function loopExpendTree(treeObject, currentNode, rootId) {
    currentNode.expanded = true
    while (currentNode.data.parentId !== rootId) {
        currentNode = treeObject.getNode(currentNode.data.parentId)
        if (!currentNode.expanded) {
            currentNode.expanded = true
        }
    }
}

