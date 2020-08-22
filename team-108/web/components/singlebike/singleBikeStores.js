class SingleBikeStores extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            error: null,
            bikeid: getUrlVars()["bikeid"],
            isLoaded: false,
            stores : [],
            page: 1
        };
    }

    componentDidMount() {
        jQuery.ajax({
            type: "GET",
            url: "api/singlebikestores",
            data: {
                bikeid: this.state.bikeid,
                limit: 25,
                offset: 25 * (this.state.page - 1)
            },
            success: (result) => {
                this.setState({
                    isLoaded: true,
                    stores: JSON.parse(result)
                })
            }
        });
    }

    updateList = (page) => {
        jQuery.ajax({
            type: "GET",
            url: "api/singlebikestores",
            data: {
                bikeid: this.state.bikeid,
                limit: 25,
                offset: 25 * (this.state.page - 1)
            },
            success: (result) => {
                this.setState({
                    isLoaded: true,
                    stores: JSON.parse(result)
                })
            }
        });
    };

    next_page = (e) => {
        e.preventDefault();
        this.state.page += 1;
        this.updateList();
    };

    prev_page = (e) => {
        e.preventDefault();
        if (this.state.page === 1) {
            return;
        } else {
            this.state.page -= 1;
            this.updateList()
        }
    };

    render() {
        if (this.state.error) {
            return <div>Error: {this.state.error.message}</div>;
        } else if (!this.state.isLoaded) {
            return <div>Loading...</div>;
        }
        return (
            <div>
                <strong>stores with this product in stock:<br/><br/></strong>
                {/*<button onClick={this.prev_page}>prev</button> Page: {this.state.page}<button onClick={this.next_page}>next</button>*/}
                <table class="table">
                    <col width="230"></col>
                    <col width="300"></col>
                    <col width="80"></col>
                    <col width="180"></col>
                    <col width="120"></col>
                    <th>store</th>
                    <th>address</th>
                    <th>quantity</th>
                    <th>email</th>
                    <th>phone</th>
                    <tbody>
                    {
                        Object.values(this.state.stores).map(
                            store => (
                                <tr>
                                    <td><a id={store.store_id}
                                           href={"/team-108/singleStore.html?storeid=" + store.store_id}>
                                            {store.store_name}
                                        </a>
                                    </td>
                                    <td>{store.address}</td>
                                    <td>{store.quantity}</td>
                                    <td>{store.email}</td>
                                    <td>{store.phone}</td>
                                </tr>
                            )
                        )
                    }
                    </tbody>
                </table>
            </div>
        );
    }

}

ReactDOM.render(<SingleBikeStores/>, document.getElementById("singleBikeStores"));