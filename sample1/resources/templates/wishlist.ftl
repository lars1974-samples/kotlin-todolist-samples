<html>
    <body>
        <h1>
        Wishlist for ${user.name}
        </h1>
        <table >
            <tr>
                <th>Id</th>
                <th>Item</th>
                <th>Price</th>
                <th>URL</th>
                <th>Shop</th>
            </tr>
        <#list user.items as item>
            <tr>
                <td>${item.id}</td>
                <td>${item.name}</td>
                <td>${item.price}</td>
                <td>${item.url}</td>
                <td>${item.shop}</td>
            </tr>
        <#else>
            There are no wishes. Please create one.
        </#list>
        </table>
    </body>
</html>