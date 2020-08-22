with open("log.txt") as f:
    res = [0,0]
    size = 0
    for line in f:
        thisline = line.rstrip().split(',')
        res[0] += float(thisline[0])
        res[1] += float(thisline[1])
        size += 1
    print("totalsize:", size, "avg TJ:", res[0]/size, "avg TS:", res[1]/size)