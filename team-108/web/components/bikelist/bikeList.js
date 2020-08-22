const Bikes = ({items}) => {

    let addToCart = (e) => {
        const id = e.target.id;
        jQuery.ajax({
            type: "GET",
            data: {
                action: "buy",
                product_id: id
            },
            url: "api/cart",
            success: (result) => {
                result = JSON.parse(result);
                console.log(result);
                alert("You now have: " + result[id].itemInfo[0].product_name + " x" + result[id].number);
            }
        })
    };

    return (
        <div>
        <table class="table">
            <col width="20"></col>
            <col width="230"></col>
            <col width="150"></col>
            <col width="150"></col>
            <col width="100"></col>
            <col width="180"></col>
            <col width="100"></col>
            <col width="100"></col>
            <col width="170"></col>
            <col width="170"></col>
            <col width="170"></col>

            <th> </th><th>product name</th><th>add to cart</th><th>brand name</th><th>year</th><th>category name</th><th>price</th><th>rating</th><th>store 1</th><th>store 2</th><th>store 3</th>
            <tbody>
                {(Object.keys(items).length !== 0) ? Object.values(items).map(
                    item => {
                        return (
                            <tr>
                                <td> </td>
                                <td>
                                    <a
                                       href={"/team-108/singleBike.html?bikeid=" + item.product_id}>
                                        {item.product_name}
                                    </a>
                                </td>
                                <td><img id={item.product_id} width="30" src="/team-108/cart.png" onClick={addToCart}/></td>
                                <td>{item.brand_name}</td>
                                <td>{item.model_year}</td>
                                <td>{item.category_name}</td>
                                <td>${item.list_price}</td>
                                <td>{item.rating}</td>
                                {(item.first3stores !== null) ? <td>
                                    {(item.first3stores['0'] !== undefined) ? <a id={ item.first3stores['0'].store_id}
                                                                                 href={"/team-108/singleStore.html?storeid=" + item.first3stores['0'].store_id}>
                                        {item.first3stores['0'].store_name}
                                    </a> : <div></div>}
                                </td> : <td></td>}
                                {(item.first3stores !== null) ? <td>
                                    {(item.first3stores['1'] !== undefined) ? <a id={ item.first3stores['1'].store_id}
                                                                                 href={"/team-108/singleStore.html?storeid=" + item.first3stores['1'].store_id}>
                                        {item.first3stores['1'].store_name}
                                    </a> : <div></div>}
                                </td> : <td></td>}
                                {(item.first3stores !== null) ? <td>
                                    {(item.first3stores['2'] !== undefined) ? <a id={ item.first3stores['2'].store_id}
                                                                                 href={"/team-108/singleStore.html?storeid=" + item.first3stores['2'].store_id}>
                                        {item.first3stores['2'].store_name}
                                    </a> : <div></div>}
                                </td> : <td></td>}
                            </tr>
                        );
                    }
                ) : <tr><td> </td><td>NO MATCH</td></tr>}
            </tbody>
        </table>
        </div>
    );
};

class BikeList extends React.Component {

    pushUrlToState = () => {

        if (Object.keys(getUrlVars()).length === 0) {

            // retrieve last search param from session
            jQuery.ajax({
                type: "POST",
                url: "api/url",
                data: {
                    action: "lookup"
                },
                async: false,
                success: (result) => {
                    let lastUrl = JSON.parse(result).url;
                    if (lastUrl !== null && lastUrl !== undefined) {
                        // window.location.href = lastUrl;
                        history.pushState("lastSearch", "BikeListUrl", lastUrl);
                    }
                }
            });
            // let lastUrl = localStorage.getItem("lastSearch");
            // if (lastUrl !== undefined) {
            //     history.pushState("lastSearch", "BikeList", lastUrl);
            // }

        }

        let keys = ["sorted", "product_name", "year", "brand", "store", "category", "page", "limit", "sort_name", "sort_rating"];
        for (let key of keys) {
            if (getUrlVars()[key] !== undefined) {
                if (key === "page" || key === "limit") {
                    this.state[key] = parseInt(getUrlVars()[key]);
                }
                else if (key === "sorted") {
                    let option = decodeURI(getUrlVars()[key]).split(";");
                    for (let i = 0; i < option.length; ++ i) {
                        let order = option[i].slice(0,1);
                        option[i] = option[i].slice(1, option[i].length);
                        if (option[i] === "rating") {
                            this.state.sort_rating = order;
                        } else {
                            this.state.sort_name = order;
                        }
                    }
                    this.state[key] = option.join(" - ");
                }
                else {
                    this.state[key] = decodeURI(getUrlVars()[key]);
                }
            }
        }

    };

    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            items: [],
            changePageDisabled : false,

            // search
            search : true,

            product_name : "",
            brand : "",
            year : "",
            store: "",

            // browse
            category : "",
            sorted : "rating - product_name",

            sort_name: "+",
            sort_rating: "-",

            page: 1,
            limit: 50
        };

        this.pushUrlToState();
        console.log(this.state);
    }

    componentDidMount() {

        let d = this.queryData();
        jQuery.ajax({
            type: "GET",
            data: d,
            url: "api/search",
            success: (result) => {

                this.setState({
                    isLoaded: true,
                    items: JSON.parse(result)
                });

                // memorize current search state
                if (d.product_name === undefined) {

                } else if (d.product_name === "%%") {
                    delete queryData.product_name;
                } else {
                    d.product_name = encodeURI(d.product_name);
                }

                jQuery.ajax({
                    url: "api/url",
                    type: "POST",
                    data: {
                        action: "save",
                        url: "/team-108/index.html" + makeParamString(d)
                    },
                    success: (result) => {
                        console.log(JSON.parse(result));
                    }
                });

                // localStorage.setItem("lastSearch", "/team-108/index.html" + makeParamString(d));
            }
        });



    }

    timer = null;

    componentWillUnmount() {
        clearTimeout(this.typingTimer);
    }

    sortingParam = () => {
        let order = this.state.sorted;
        let mapping = {
            product_name: this.state.sort_name,
            rating: this.state.sort_rating
        };

        let option = order.split(" - ");
        for (let i = 0; i < option.length; ++i) {
            option[i] = mapping[option[i]] + option[i];
        }
        return option.join(";");
    };

    queryData = () => {

        // return query data from state

        let d = {};
        if (this.state.product_name !== "")
            d.product_name = this.state.product_name;
        if (this.state.brand !== "")
            d.brand = this.state.brand;
        if (this.state.year !== "")
            d.year = this.state.year;
        if (this.state.store !== "")
            d.store = this.state.store;
        if (this.state.category !== "")
            d.category = this.state.category;
        d.sorted = this.sortingParam();
        d.page = this.state.page;
        d.limit = this.state.limit;
        d.offset = (this.state.page - 1) * this.state.limit;
        return d;
    };

    after = () => {
        // console.log(this.state);

        let d = this.queryData();
        console.log(d);

        jQuery.ajax({
            type: "GET",
            url: "api/search",
            data: d,
            success: (result) => {
                console.log(result);
                this.setState({
                    isLoaded: true,
                    items: JSON.parse(result),
                    changePageDisabled: false
                });

                let queryData = this.queryData();
                if (queryData.product_name === "%%") {
                    delete queryData.product_name;
                } else if (queryData.product_name === undefined) {
                    // do nothing
                } else {
                    queryData.product_name = encodeURI(queryData.product_name);
                }

                // modify url

                history.pushState(
                    "modified",
                    "BikeList-Mod",
                    "/team-108/index.html" + makeParamString(queryData)
                );

                // save current search state to session

                jQuery.ajax({
                    url: "api/url",
                    type: "POST",
                    data: {
                        action: "save",
                        url: "/team-108/index.html" + makeParamString(queryData)
                    },
                    success: (result) => {
                        console.log(JSON.parse(result));
                    }
                });

            }
        });
    };

    handleChange = (e) => {
        e.preventDefault();

        const value = e.target.value;
        const id = e.target.id;
        clearTimeout(this.typingTimer);
        this.typingTimer = setTimeout(() => {

            if (id === "product_name") {
                this.setState({
                    product_name: "%" + value + "%"
                }, this.after);
            } else if (id === "category" && value === "all") {
                this.setState({
                    category : ""
                }, this.after);
            } else if (id === "sort_name" || id === "sort_rating") {

                if (this.state[id] === "+") {
                    this.setState({
                        [id] : "-"
                    }, this.after);
                } else {
                    this.setState({
                        [id] : "+"
                    }, this.after);
                }

            } else {
                this.setState({
                        [id]: value
                    }, this.after
                );
            }

        }, 500);
    };



    changePage = (e) => {
        e.preventDefault();

        this.setState({
            changePageDisabled: true
        });

        const id = e.target.id;
        console.log(id);

        if (id === "next") {
            this.setState(
                {
                    page: this.state.page + 1
                }
                , this.after
            );
        } else if (this.state.page === 1) {
            this.setState({
                changePageDisabled: false
            });
        } else {
            this.setState(
                {
                    page: this.state.page - 1
                }
                , this.after
            );
        }

        // clearTimeout(this.typingTimer);
        // this.typingTimer = setTimeout(
        //     () => {
        //
        //
        //
        //     }, 500
        // );


    };


    toggleSearch = (e) => {
        if (this.state.search) {
            this.setState({search: false});
        } else {
            this.setState({search: true});
        }
    };

    render() {
        const { error, isLoaded, items } = this.state;
        if (error) {
            return <div>Error: {error.message}</div>;
        } else if (!isLoaded) {
            return <div className="loading" align="center">Loading...</div>;
        }
        else {
            return (
                <div>
                    <table>
                        <col width="auto"></col>
                        <col width="60px"></col>
                        <col width={(this.state.search) ? "260px" : "0px"} />
                        <tr valign="top">

                            <td><Bikes items={items} /></td>
                            <td>

                            </td>
                            <td>

                                {(this.state.search) ? <div className="search">
                                    <table class="table">
                                        <th>searching and browsing</th>
                                        <tr>
                                            <td>
                                                <table>
                                                    <tr>
                                                        <td>product: </td>
                                                        <td><input id="product_name" type="text" defaultValue={this.state.product_name.split("%").join("")} onChange={this.handleChange}/></td>
                                                    </tr>
                                                    <tr>
                                                        <td>brand:</td>
                                                        <td><input id="brand" type="text" defaultValue={this.state.brand} onChange={this.handleChange}/></td>
                                                    </tr>
                                                    <tr>
                                                        <td>year:</td>
                                                        <td><input id="year" type="text" defaultValue={this.state.year} onChange={this.handleChange}/></td>
                                                    </tr>
                                                    <tr>
                                                        <td>store:</td>
                                                        <td><input id="store" type="text" defaultValue={this.state.store} onChange={this.handleChange}/></td>
                                                    </tr>
                                                    <tr>
                                                        <td>category:</td>
                                                        <td>
                                                            <select id="category" type="text" defaultValue={this.state.category} onChange={this.handleChange}>
                                                                <option>all</option>
                                                                <option>Children Bicycles</option>
                                                                <option>Comfort Bicycles</option>
                                                                <option>Cruisers Bicycles</option>
                                                                <option>Cyclocross Bicycles</option>
                                                                <option>Electric Bikes</option>
                                                                <option>Mountain Bikes</option>
                                                                <option>Road Bikes</option>
                                                                <option>Time Trial Bikes</option>
                                                                <option>Trick Bikes</option>
                                                                <option>Commuting Bikes</option>
                                                                <option>Track Bikes</option>
                                                                <option>Tandems</option>
                                                                <option>Adult Trikes</option>
                                                                <option>Folding Bike</option>
                                                                <option>Recumbents</option>
                                                            </select>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            sort order:
                                                        </td>
                                                        <select id="sorted" type="text" defaultValue={this.state.sorted} onChange={this.handleChange}>
                                                            <option>rating - product_name</option>
                                                            <option>product_name - rating</option>
                                                        </select>

                                                    </tr>
                                                    <tr>
                                                        <td>sort name:</td>
                                                        <td><button id="sort_name" onClick={this.handleChange}>{(this.state.sort_name === "+") ? "asec" : "desc"}</button></td>
                                                    </tr>
                                                    <tr>
                                                        <td>sort rating:</td>
                                                        <td><button id="sort_rating" onClick={this.handleChange}>{(this.state.sort_rating === "+") ? "asec" : "desc"}</button></td>
                                                    </tr>
                                                    <tr>
                                                        <td>max display:</td>
                                                        <td>
                                                            <select id="limit" type="text" defaultValue={this.state.limit} onChange={this.handleChange}>
                                                                <option>50</option>
                                                                <option>10</option>
                                                                <option>25</option>
                                                                <option>100</option>
                                                            </select>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            Page:
                                                        </td>
                                                        <td>
                                                            {this.state.page}
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <button id="prev" disabled={this.state.changePageDisabled} onClick={this.changePage}>Prev</button>
                                                        </td>
                                                        <td>
                                                            <button id="next" disabled={this.state.changePageDisabled} onClick={this.changePage}>Next</button>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                </div> : <div/>}
                                {/*<button onClick={this.toggleSearch}>Toggle Search Bar</button>*/}
                            </td>
                        </tr>
                    </table>
                </div>
            );
        }
    }

}

ReactDOM.render(<BikeList />, document.getElementById("bikeList"));
