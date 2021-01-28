package com.easypay.service;

import com.easypay.domain.Bill;
import com.easypay.domain.Location;
import com.easypay.repository.BillRepository;
import com.easypay.service.dto.BillDTO;
import com.easypay.service.dto.PayBillsDTO;
import com.easypay.service.mapper.BillMapper;
import com.easypay.service.mapper.LocationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Bill}.
 */
@Service
@Transactional
public class BillService {

    private final Logger log = LoggerFactory.getLogger(BillService.class);

    private final BillRepository billRepository;

    private final BillMapper billMapper;

    private final LocationMapper locationMapper;

    public BillService(BillRepository billRepository,
                       BillMapper billMapper,
                       LocationMapper locationMapper
    ) {
        this.billRepository = billRepository;
        this.billMapper = billMapper;
        this.locationMapper = locationMapper;
    }

    /**
     * Save a bill.
     *
     * @param billDTO the entity to save.
     * @return the persisted entity.
     */
    public BillDTO save(BillDTO billDTO) {
        log.debug("Request to save Bill : {}", billDTO);
        Bill bill = billMapper.toEntity(billDTO);
        bill = billRepository.save(bill);
        return billMapper.toDto(bill);
    }

    /**
     * Save a bill.
     *
     * @return the persisted entity.
     */
    public void saveABill(Bill bill) {
        billRepository.save(bill);
    }

    /**
     * Get all the bills.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BillDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Bills");
        return billRepository.findAll(pageable)
                             .map(billMapper::toDto);
    }


    /**
     * Get one bill by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BillDTO> findOne(Long id) {
        log.debug("Request to get Bill : {}", id);
        return billRepository.findById(id)
                             .map(billMapper::toDto);
    }

    /**
     * Delete the bill by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Bill : {}", id);
        billRepository.deleteById(id);
    }

    public List<Bill> findByLocation(Location location, Boolean paid) {

        return billRepository.findByLocationAndPaid(location, paid);
    }

    public List<Bill> findAllByLocation(Location location) {
        return billRepository.findAllByLocation(location);
    }

    public Optional<Bill> deleteByLocationAndFirstDayAndLastDayAndValue(Location location, PayBillsDTO payBillsDTO) {
        return billRepository.findByFirstDayAndLastDayAndValue(payBillsDTO.getFirstDay(),
                                                                          payBillsDTO.getLastDay(),
                                                                          payBillsDTO.getValue()
        );
    }
}
