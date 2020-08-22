class SingleStore extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            error: null,
            storeid: getUrlVars()["storeid"],
            isLoaded: false,
            storeinfo: {

            }
        };
    }

    componentDidMount() {
        jQuery.ajax({
            type: "GET",
            url: "api/singlestore",
            data: {
                storeid: this.state.storeid
            },
            success: (result) => {
                this.setState({
                    isLoaded: true,
                    storeinfo: JSON.parse(result)[0]
                })
            }
        });
    }

    render() {
        if (this.state.error) {
            return <div>Error: {this.state.error.message}</div>;
        } else if (!this.state.isLoaded) {
            return <div className="loading">Loading...</div>;
        }
        return (
            <div>
                <table className="table">
                    <col width="200"></col>
                    <col width="200"></col>
                    <th><strong><br/>store info:</strong></th>
                    <th></th>
                    <tbody>
                    {
                        Object.keys(this.state.storeinfo).map(
                            key => (
                                <tr>
                                    <td>{key}</td>
                                    <td>{this.state.storeinfo[key]}</td>
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

ReactDOM.render(<SingleStore/>, document.getElementById("singleStore"));