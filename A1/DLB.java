public class DLB implements DictInterface{
    char character;
    DLB horizontal = null;
    DLB child = null;
    boolean isWord;    

    public DLB addNode(char c)
    {
        //already has a child added
        if(child!=null)
        {
            return child.horizontalAdd(c);
        }
        else
        {
            this.child = new DLB();
            this.child.character = c;
            return this.child; 
        }

    }

    public boolean add(String word)
    {

        DLB next = this.addNode(word.charAt(0));
        if(!(word.substring(1).length()<1))
        {

            next.add(word.substring(1));


        }
        else
        {
            next.isWord = true;
        }
        return true;
    }

    public DLB horizontalAdd(char c)
    {
        //This is the node with the right character
        if(c==character)
        {
            return this;
        }
        //looks in the next node over
        else if(horizontal != null)
        {
            return horizontal.horizontalAdd(c);
        }
        //if this node doesn't have it, and the next node is null, its not found
        else
        {
            DLB newSibling = new DLB();
            newSibling.character = c;
            this.horizontal = newSibling;
            return newSibling;
        }
    }

    public DLB getNode(char c)
    {
        //This is the node with the right character
        if(c==character)
        {
            return this;
        }
        //looks in the next node over
        else if(horizontal != null)
        {
            return horizontal.getNode(c);
        }
        //if this node doesn't have it, and the next node is null, its not found
        else
        {
            return null;
        }

    }

    public int searchPrefix(StringBuilder s)
    {
      
        if(s.length()== 0) //reached end of word
        {
            if(this.isWord == true)//is a word
            {
                if(this.child != null) //is a word and prefix
                {
                    return 3;
                }
                else //is only a word
                {
                    return 2;
                }
            }
            else //is only a prefix
            {
                return 1;
            }
        }
        else// more characters to search
        {
            String suffix = s.substring(1);
            char search = s.charAt(0);
            if(this.child != null)
            {
                DLB next = this.child.getNode(search);

                if(next == null)
                {
                    return 0;
                }

                s = new StringBuilder(suffix);
                return next.searchPrefix(s);
            }
            else
            {
                return 0;
            }
        }
    }

    public int searchPrefix(StringBuilder s, int start, int end)
    {
        String str = s.substring(start, end);
        s = new StringBuilder(str);
        return this.searchPrefix(s);
    }

}