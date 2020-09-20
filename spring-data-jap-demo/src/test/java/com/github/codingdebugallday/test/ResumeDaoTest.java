package com.github.codingdebugallday.test;

import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import com.github.codingdebugallday.dao.ResumeDao;
import com.github.codingdebugallday.pojo.Resume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/09/21 0:47
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"classpath*:applicationContext.xml"}
)
public class ResumeDaoTest {

    @Autowired
    private ResumeDao resumeDao;

    //===============================================================================
    //
    // dao层接口调用，分成两块：
    // 1、基础的增删改查
    // 2、专门针对查询的详细分析使用
    //
    //===============================================================================

    @Test
    public void testFindById() {
        /*
           select resume0_.id as id1_0_0_,
           resume0_.address as address2_0_0_,
            resume0_.name as name3_0_0_,
            resume0_.phone as phone4_0_0_
            from resume resume0_
            where resume0_.id=?
         */
        Optional<Resume> optional = resumeDao.findById(1L);
        if (optional.isPresent()) {
            Resume resume = optional.get();
            System.out.println(resume);
        } else {
            System.out.println("not find");
        }
    }

    @Test
    public void testFindOne() {
        Resume resume = new Resume();
        resume.setId(1L);
        resume.setName("张三");
        Example<Resume> example = Example.of(resume);
        Optional<Resume> one = resumeDao.findOne(example);
        if (one.isPresent()) {
            System.out.println(one.get());
        } else {
            System.out.println("not find");
        }
    }

    @Test
    public void testSave() {
        // 新增和更新都使用save方法，通过传入的对象的主键有无来区分，没有主键信息那就是新增，有主键信息就是更新
        Resume resume = new Resume();
        resume.setId(4L);
        resume.setName("赵六六");
        resume.setAddress("成都");
        resume.setPhone("13200000000");
        Resume save = resumeDao.save(resume);
        System.out.println(save);
    }

    @Test
    public void testDelete() {
        resumeDao.deleteById(4L);
    }


    @Test
    public void testFindAll() {
        List<Resume> list = resumeDao.findAll();
        for (Resume resume : list) {
            System.out.println(resume);
        }
    }

    @Test
    public void testSort() {
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "id"));
        List<Resume> list = resumeDao.findAll(sort);
        for (Resume resume : list) {
            System.out.println(resume);
        }
    }

    @Test
    public void testPage() {
        /*
          第一个参数：当前查询的页数，从0开始
          第二个参数：每页查询的数量
        */
        Pageable pageable = PageRequest.of(0, 2);
        Page<Resume> all = resumeDao.findAll(pageable);
        System.out.println("总数：" + all.getTotalElements());
        System.out.println("总页数：" + all.getTotalPages());
        System.out.println("当前页：" + all.getPageable());
        List<Resume> list = all.getContent();
        for (Resume resume : list) {
            System.out.println(resume);
        }
    }

    /**
     * ========================针对查询的使用进行分析=======================
     * 方式一：调用继承的接口中的方法  findOne(),findById()
     * 方式二：可以引入jpql（jpa查询语言）语句进行查询 (=====>>>> jpql 语句类似于sql，只不过sql操作的是数据表和字段，jpql操作的是对象和属性，比如 from Resume where id=xx)  hql
     * 方式三：可以引入原生的sql语句
     * 方式四：可以在接口中自定义方法，而且不必引入jpql或者sql语句，这种方式叫做方法命名规则查询，也就是说定义的接口方法名是按照一定规则形成的，那么框架就能够理解我们的意图
     * 方式五：动态查询
     * service层传入dao层的条件不确定，把service拿到条件封装成一个对象传递给Dao层，这个对象就叫做Specification（对条件的一个封装）
     * <p>
     * <p>
     * // 根据条件查询单个对象
     * Optional<T> findOne(@Nullable Specification<T> var1);
     * // 根据条件查询所有
     * List<T> findAll(@Nullable Specification<T> var1);
     * // 根据条件查询并进行分页
     * Page<T> findAll(@Nullable Specification<T> var1, Pageable var2);
     * // 根据条件查询并进行排序
     * List<T> findAll(@Nullable Specification<T> var1, Sort var2);
     * // 根据条件统计
     * long count(@Nullable Specification<T> var1);
     * <p>
     * interface Specification<T>
     * toPredicate(Root<T> var1, CriteriaQuery<?> var2, CriteriaBuilder var3);用来封装查询条件的
     * Root:根属性（查询所需要的任何属性都可以从根对象中获取）
     * CriteriaQuery 自定义查询方式 用不上
     * CriteriaBuilder 查询构造器，封装了很多的查询条件（like = 等）
     */

    @Test
    public void testJpql() {
        List<Resume> list = resumeDao.findByJpql(1L, "张三");
        for (Resume resume : list) {
            System.out.println(resume);
        }
    }

    @Test
    public void testSql() {
        List<Resume> list = resumeDao.findBySql("李%", "上海%");
        for (Resume resume : list) {
            System.out.println(resume);
        }
    }

    @Test
    public void testMethodName() {
        List<Resume> list = resumeDao.findByNameLikeAndAddress("李%", "上海");
        for (Resume resume : list) {
            System.out.println(resume);
        }
    }

    // 动态查询，查询单个对象
    @Test
    public void testSpecification() {

        /*
         * 动态条件封装
         * 匿名内部类
         *
         * toPredicate：动态组装查询条件
         *
         *      借助于两个参数完成条件拼装，，， select * from tb_resume where name='张三'
         *      Root: 获取需要查询的对象属性
         *      CriteriaBuilder：构建查询条件，内部封装了很多查询条件（模糊查询，精准查询）
         *
         *      需求：根据name（指定为"张三"）查询简历
         */
        Specification<Resume> specification =
                (root, criteriaQuery, criteriaBuilder) -> {
                    // 获取到name属性
                    Path<Object> name = root.get("name");
                    // 使用CriteriaBuilder针对name属性构建条件（精准查询）
                    return criteriaBuilder.equal(name, "张三");
                };
        Optional<Resume> optional = resumeDao.findOne(specification);
        if (optional.isPresent()) {
            System.out.println(optional.get());
        } else {
            System.out.println("not find");
        }
    }

    @Test
    public void testSpecificationMultiCon() {
        /*
         *  需求：根据name（指定为"张三"）并且，address 以"北"开头（模糊匹配），查询简历
         */
        Specification<Resume> specification =
                (root, criteriaQuery, criteriaBuilder) -> {
                    // 获取到name属性
                    Path<Object> name = root.get("name");
                    Path<Object> address = root.get("address");
                    // 条件1：使用CriteriaBuilder针对name属性构建条件（精准查询）
                    Predicate predicate1 = criteriaBuilder.equal(name, "张三");
                    // 条件2：address 以"北"开头（模糊匹配）
                    Predicate predicate2 = criteriaBuilder.like(address.as(String.class), "北%");
                    // 组合两个条件
                    return criteriaBuilder.and(predicate1, predicate2);
                };
        Optional<Resume> optional = resumeDao.findOne(specification);
        if (optional.isPresent()) {
            System.out.println(optional.get());
        } else {
            System.out.println("not find");
        }
    }

}
