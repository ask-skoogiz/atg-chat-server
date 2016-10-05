/**
 * 
 */
package ask.atg.chat.server.model;

/**
 * Model to represent the contact connection between two {@link User}s.
 * 
 * @author Anders Skoglund
 *
 */
public abstract class Contact
{
    /**
     * Enum representing the different types of {@link Contact}s available.
     */
    public enum Type
    {
        REQUEST,
        MUTUAL;
    }

    /**
     * Static factory method.
     * 
     * @param fromUser
     * @param toUser
     * @return a new instance of {@link Contact}
     */
    public static Contact create(final User fromUser, final User toUser)
    {
        return new ContactBuilder(fromUser, toUser).build();
    }

    /**
     * Get the current {@link Type} of the contact.
     * 
     * @return
     */
    public abstract Type getType();

    /**
     * Get one end of the {@link Contact}.
     *
     * @return
     */
    public abstract User fromUser();

    /**
     * Get another end of the {@link Contact}.
     *
     * @return
     */
    public abstract User toUser();

    /**
     * Check if a {@link User} is part off the {@link Contact}.
     *
     * @param user
     * @return true if {@link User} is part of the {@link Contact}
     */
    public boolean contains(User user)
    {
        return fromUser().is(user) || toUser().is(user);
    }

    /**
     * Check if contact is between the two {@link User}s.
     *
     * @param anUser
     * @param anotherUser
     * @return true if connected to each other
     */
    public boolean isBetween(User anUser, User anotherUser)
    {
        return (fromUser().equals(anUser) && toUser().equals(anotherUser)) ||
            (fromUser().equals(anotherUser) && toUser().equals(anUser));
    }

    /**
     * Immutable change of {@link Type}.
     * 
     * @param type
     * @return a new instance of {@link Contact}
     */
    public Contact replace(Type type)
    {
        return new ContactBuilder(fromUser(), toUser()).type(type).build();
    }

    public static class ContactBuilder
    {
        private final User fromUser;
        private final User toUser;

        private Type type = Type.REQUEST;

        public ContactBuilder(User fromUser, User toUser)
        {
            this.fromUser = fromUser;
            this.toUser = toUser;
        }

        public ContactBuilder type(Type type)
        {
            this.type = type;
            return this;
        }

        public Contact build()
        {
            return new Contact()
            {

                @Override
                public Type getType()
                {
                    return type;
                }

                @Override
                public User fromUser()
                {
                    return fromUser;
                }

                @Override
                public User toUser()
                {
                    return toUser;
                }
            };
        }
    }
}
