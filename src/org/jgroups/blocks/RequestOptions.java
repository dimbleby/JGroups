package org.jgroups.blocks;

import org.jgroups.Address;
import org.jgroups.Message;

import java.util.Arrays;

/** Class which captures a bunch of options relevant to remote method invocation or message sending
 * @author Bela Ban
 * @since 2.10
 */
public class RequestOptions {
    /** The mode of a request. Defined in {@link ResponseMode} e.g. GET_NONE, GET_ALL */
    protected ResponseMode  mode=ResponseMode.GET_ALL;

    /** The max time (in ms) for a blocking call. 0 blocks until all responses have been received (if mode = GET_ALL) */
    protected long          timeout; // used when mode != GET_NONE

    /** Turns on anycasting; this results in multiple unicasts rather than a multicast for group calls */
    protected boolean       use_anycasting;

    /** If use_anycasting is true: do we want to use an AnycastAddress [B,C] or a unicast to B and another unicast
     * to C to send an anycast to {B,C} ? Only used if use_anycasting is true */
    protected boolean       use_anycast_addresses;

    /** Allows for filtering of responses */
    protected RspFilter     rsp_filter;

    /** The flags set in the message in which a request is sent */
    protected short         flags; // Message.Flag.OOB, Message.Flag.DONT_BUNDLE etc

    protected short         transient_flags;

    /** A list of members which should be excluded from a call */
    protected Address[]     exclusion_list;



    public RequestOptions() {
    }

    public RequestOptions(ResponseMode mode, long timeout, boolean use_anycasting, RspFilter rsp_filter, Message.Flag ... flags) {
        this(mode, timeout, use_anycasting, rsp_filter,(short)0);
        flags(flags);
    }

    public RequestOptions(ResponseMode mode, long timeout, boolean use_anycasting, RspFilter rsp_filter, short flags) {
        this.mode=mode;
        this.timeout=timeout;
        this.use_anycasting=use_anycasting;
        this.rsp_filter=rsp_filter;
        this.flags=flags;
    }

    public RequestOptions(ResponseMode mode, long timeout, boolean use_anycasting, RspFilter rsp_filter) {
        this(mode, timeout, use_anycasting, rsp_filter, (Message.Flag[])null);
    }

    public RequestOptions(ResponseMode mode, long timeout) {
        this(mode, timeout, false, null);
    }

    public RequestOptions(ResponseMode mode, long timeout, boolean use_anycasting) {
        this(mode, timeout, use_anycasting, null);
    }

    public RequestOptions(RequestOptions opts) {
        this.mode=opts.mode;
        this.timeout=opts.timeout;
        this.use_anycasting=opts.use_anycasting;
        this.rsp_filter=opts.rsp_filter;
        this.flags=opts.flags;
        this.transient_flags=opts.transient_flags;
        this.exclusion_list=opts.exclusion_list;
    }


    public static RequestOptions SYNC() {return new RequestOptions(ResponseMode.GET_ALL, 10000);}
    public static RequestOptions ASYNC() {return new RequestOptions(ResponseMode.GET_NONE, 10000);}


    public ResponseMode mode() {
        return mode;
    }

    public RequestOptions mode(ResponseMode mode) {
        this.mode=mode;
        return this;
    }

    public long timeout() {
        return timeout;
    }

    public RequestOptions timeout(long timeout) {
        this.timeout=timeout;
        return this;
    }

    public boolean anycasting() {
        return use_anycasting;
    }

    public RequestOptions anycasting(boolean use_anycasting) {
        this.use_anycasting=use_anycasting;
        return this;
    }


    public boolean useAnycastAddresses() {return use_anycast_addresses;}

    public RequestOptions useAnycastAddresses(boolean flag) {
        use_anycast_addresses=flag;
        return this;
    }

    public RspFilter rspFilter() {
        return rsp_filter;
    }

    public RequestOptions rspFilter(RspFilter rsp_filter) {
        this.rsp_filter=rsp_filter;
        return this;
    }

    public short flags() {
        return flags;
    }

    public short transientFlags() {return transient_flags;}

    public boolean flagSet(Message.Flag flag) {
        return flag != null && ((flags & flag.value()) == flag.value());
    }

    public boolean transientFlagSet(Message.TransientFlag flag) {return flag != null && ((transient_flags & flag.value()) == flag.value());}

    public RequestOptions flags(Message.Flag ... flags) {
        if(flags != null)
            for(Message.Flag flag: flags)
                if(flag != null)
                    this.flags |= flag.value();
        return this;
    }

    public RequestOptions transientFlags(Message.TransientFlag ... flags) {
        if(flags != null)
            for(Message.TransientFlag flag: flags)
                if(flag != null)
                    this.transient_flags |= flag.value();
        return this;
    }

    public RequestOptions clearFlags(Message.Flag ... flags) {
        if(flags != null)
            for(Message.Flag flag: flags)
                if(flag != null)
                    this.flags &= ~flag.value();
        return this;
    }

    public RequestOptions clearTransientFlags(Message.TransientFlag ... flags) {
        if(flags != null)
            for(Message.TransientFlag flag: flags)
                if(flag != null)
                    this.transient_flags &= ~flag.value();
        return this;
    }

    public boolean hasExclusionList() {
        return exclusion_list != null;
    }


    public Address[] exclusionList() {
        return exclusion_list;
    }

    public RequestOptions exclusionList(Address ... mbrs) {
        if(mbrs == null || mbrs.length == 0)
            return this;
        exclusion_list=mbrs;
        return this;
    }


    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("mode=" + mode).append(", timeout=" + timeout);
        if(use_anycasting) {
            sb.append(", anycasting=true");
            if(use_anycast_addresses)
                sb.append(" (using AnycastAddress)");
        }
        if(flags > 0)
            sb.append(", flags=" + Message.flagsToString(flags));
        if(transient_flags > 0)
            sb.append(", transient_flags=" + Message.transientFlagsToString(transient_flags));
        if(exclusion_list != null)
            sb.append(", exclusion list: " + Arrays.toString(exclusion_list));
        return sb.toString();
    }

}
