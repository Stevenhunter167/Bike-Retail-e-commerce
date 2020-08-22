class Searching extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            // product_name: "",
            // brand: "",
            // year: "",
            // store: "",
            // category: "",
            limit: 50,
            sorted: "-rating;+product_name"
        };
    }

    handleSubmit = (e) => {
        e.preventDefault();
        window.location.href = "/team-108/index.html" + encodeURI(makeParamString(this.state));
    };

    handleChange = (e) => {
        e.preventDefault();
        if (e.target.value == "") {
            delete this.state[e.target.id];
        } else if (e.target.id === "product_name") {
            this.setState({
                product_name: "%" + e.target.value + "%"
            });
        } else {
            this.setState({
                [e.target.id]: e.target.value
            });
        }
    };

    render() {
        return (
            <div>
                <h4 align="center">searching and browsing</h4>
                <table>
                    <tr>
                        <td>product:</td>
                        <td><input id="product_name" name="product_name" onChange={this.handleChange} type="text"/></td>
                    </tr>
                    <tr>
                        <td>brand:</td>
                        <td><input id="brand" name="brand" onChange={this.handleChange} type="text"/></td>
                    </tr>
                    <tr>
                        <td>year:</td>
                        <td><input id="year" name="year" onChange={this.handleChange} type="text"/></td>
                    </tr>
                    <tr>
                        <td>store:</td>
                        <td><input id="store" name="store" onChange={this.handleChange} type="text"/></td>
                    </tr>
                    <tr>
                        <td>category:</td>
                        <td>
                            <select id="category" onChange={this.handleChange}>
                                <option></option>
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
                        <td>max display:</td>
                        <td>
                            <select id="limit" onChange={this.handleChange}>
                                <option>50</option>
                                <option>10</option>
                                <option>25</option>
                                <option>100</option>
                            </select>
                        </td>
                    </tr>
                </table>
                <div align="center">
                    <button onClick={this.handleSubmit}>submit</button>
                </div>
            </div>
        );
    }
};

ReactDOM.render(<Searching/>, document.getElementById("searching"));