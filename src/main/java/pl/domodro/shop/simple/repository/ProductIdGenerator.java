package pl.domodro.shop.simple.repository;

import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class ProductIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        if (!(object instanceof ProductEntity)) {
            throw new HibernateException("Object is not of type Product");
        }
        ProductEntity product = (ProductEntity) object;
        return product.getName().toLowerCase().replaceAll("\\s", "_");
    }
}
