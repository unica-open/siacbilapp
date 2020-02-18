/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociare;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;

/**
 * Classe di model per l'associazione tra documento ed allegato atto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/set/2014
 * @version 1.0.1 - 30/set/2014 - aggiunta classe base
 */
public class AssociaDocumentoAllegatoAttoModel extends GenericAssociaAllegatoAttoModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7057075497086725361L;

	private static final int ELEMENTI_PER_PAGINA_QUOTA = 50;
	
	private TipoFamigliaDocumento tipoFamigliaDocumento;
	private TipoDocumento tipoDocumento;
	// Documento (entrata + spesa)
	private Integer annoDocumento;
	private String numeroDocumento;
	private Integer numeroSubdocumento;
	private Date dataEmissioneDocumento;
	// Movimento Gestione (impegno + accertamento)
	private Integer annoMovimento;
	private BigDecimal numeroMovimento;
	
	// Step 2
	private BigDecimal totaleSubdocumentiSpesa;
	private BigDecimal totaleSubdocumentiEntrata;
	
	private List<TipoDocumento> listaTipoDocumento = new ArrayList<TipoDocumento>();
	private Map<Integer, String> mappaUidSubdocumenti = new HashMap<Integer, String>();
	
	//SIAC-6840
	private String codiceAvvisoPagoPA;
	
	//SIAC-4440
	private boolean gestioneResiduiDisabilitata;
	
	/** Costruttore vuoto di default */
	public AssociaDocumentoAllegatoAttoModel() {
		super();
		setTitolo("Associa documenti allegato atto");
	}

	/**
	 * @return the tipoFamigliaDocumento
	 */
	public TipoFamigliaDocumento getTipoFamigliaDocumento() {
		return tipoFamigliaDocumento;
	}

	/**
	 * @param tipoFamigliaDocumento the tipoFamigliaDocumento to set
	 */
	public void setTipoFamigliaDocumento(TipoFamigliaDocumento tipoFamigliaDocumento) {
		this.tipoFamigliaDocumento = tipoFamigliaDocumento;
	}

	/**
	 * @return the tipoDocumento
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento the tipoDocumento to set
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @return the annoDocumento
	 */
	public Integer getAnnoDocumento() {
		return annoDocumento;
	}

	/**
	 * @param annoDocumento the annoDocumento to set
	 */
	public void setAnnoDocumento(Integer annoDocumento) {
		this.annoDocumento = annoDocumento;
	}

	/**
	 * @return the numeroDocumento
	 */
	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	/**
	 * @param numeroDocumento the numeroDocumento to set
	 */
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	/**
	 * @return the numeroSubdocumento
	 */
	public Integer getNumeroSubdocumento() {
		return numeroSubdocumento;
	}

	/**
	 * @param numeroSubdocumento the numeroSubdocumento to set
	 */
	public void setNumeroSubdocumento(Integer numeroSubdocumento) {
		this.numeroSubdocumento = numeroSubdocumento;
	}

	/**
	 * @return the dataEmissioneDocumento
	 */
	public Date getDataEmissioneDocumento() {
		return dataEmissioneDocumento == null ? null : new Date(dataEmissioneDocumento.getTime());
	}

	/**
	 * @param dataEmissioneDocumento the dataEmissioneDocumento to set
	 */
	public void setDataEmissioneDocumento(Date dataEmissioneDocumento) {
		this.dataEmissioneDocumento = dataEmissioneDocumento == null ? null : new Date(dataEmissioneDocumento.getTime());
	}

	/**
	 * @return the annoMovimento
	 */
	public Integer getAnnoMovimento() {
		return annoMovimento;
	}

	/**
	 * @param annoMovimento the annoMovimento to set
	 */
	public void setAnnoMovimento(Integer annoMovimento) {
		this.annoMovimento = annoMovimento;
	}

	/**
	 * @return the numeroMovimento
	 */
	public BigDecimal getNumeroMovimento() {
		return numeroMovimento;
	}

	/**
	 * @param numeroMovimento the numeroMovimento to set
	 */
	public void setNumeroMovimento(BigDecimal numeroMovimento) {
		this.numeroMovimento = numeroMovimento;
	}
	
	/**
	 * @return the totaleSubdocumentiSpesa
	 */
	public BigDecimal getTotaleSubdocumentiSpesa() {
		return totaleSubdocumentiSpesa;
	}

	/**
	 * @param totaleSubdocumentiSpesa the totaleSubdocumentiSpesa to set
	 */
	public void setTotaleSubdocumentiSpesa(BigDecimal totaleSubdocumentiSpesa) {
		this.totaleSubdocumentiSpesa = totaleSubdocumentiSpesa;
	}

	/**
	 * @return the totaleSubdocumentiEntrata
	 */
	public BigDecimal getTotaleSubdocumentiEntrata() {
		return totaleSubdocumentiEntrata;
	}

	/**
	 * @param totaleSubdocumentiEntrata the totaleSubdocumentiEntrata to set
	 */
	public void setTotaleSubdocumentiEntrata(BigDecimal totaleSubdocumentiEntrata) {
		this.totaleSubdocumentiEntrata = totaleSubdocumentiEntrata;
	}

	/**
	 * @return the listaTipoDocumento
	 */
	public List<TipoDocumento> getListaTipoDocumento() {
		return listaTipoDocumento;
	}

	/**
	 * @param listaTipoDocumento the listaTipoDocumento to set
	 */
	public void setListaTipoDocumento(List<TipoDocumento> listaTipoDocumento) {
		this.listaTipoDocumento = listaTipoDocumento != null ? listaTipoDocumento : new ArrayList<TipoDocumento>();
	}
	
	/**
	 * @return the listaTipoDocumentoFiltrato
	 */
	public List<TipoDocumento> getListaTipoDocumentoFiltrato() {
		List<TipoDocumento> result = new ArrayList<TipoDocumento>(getListaTipoDocumento());
		if(getTipoFamigliaDocumento() != null) {
			TipoFamigliaDocumento tfd = getTipoFamigliaDocumento();
			ListIterator<TipoDocumento> it = result.listIterator();
			while(it.hasNext()) {
				TipoDocumento td = it.next();
				if(!tfd.equals(td.getTipoFamigliaDocumento())) {
					it.remove();
				}
			}
		}
		return result;
	}
	
	/**
	 * @return the mappaUidSubdocumenti
	 */
	public Map<Integer, String> getMappaUidSubdocumenti() {
		return mappaUidSubdocumenti;
	}

	/**
	 * @param mappaUidSubdocumenti the mappaUidSubdocumenti to set
	 */
	public void setMappaUidSubdocumenti(Map<Integer, String> mappaUidSubdocumenti) {
		this.mappaUidSubdocumenti = mappaUidSubdocumenti != null ? mappaUidSubdocumenti : new HashMap<Integer, String>();
	}
	
	/**
	 * @return gestioneResiduiDisabilitata
	 */
	public boolean getGestioneResiduiDisabilitata() {
		return gestioneResiduiDisabilitata;
	}

	/**
	 * @param gestioneResiduiDisabilitata the gestioneResiduiDisabilitata to set
	 */
	public void setGestioneResiduiDisabilitata(boolean gestioneResiduiDisabilitata) {
		this.gestioneResiduiDisabilitata = gestioneResiduiDisabilitata;
	}
	
	/**
	 * @param codiceAvvisoPagoPA
	 */
	public String getCodiceAvvisoPagoPA() {
		return codiceAvvisoPagoPA;
	}
	
	/**
	 * @param codiceAvvisoPagoPA the codiceAvvisoPagoPA to set
	 */
	public void setCodiceAvvisoPagoPA(String codiceAvvisoPagoPA) {
		this.codiceAvvisoPagoPA = codiceAvvisoPagoPA;
	}
	
	/* **** Request **** */


	@Override
	protected void popolaElencoDocumentiAllegato(ElencoDocumentiAllegato elencoDocumentiAllegato) {
		elencoDocumentiAllegato.setAnno(getAnnoEsercizioInt());
		elencoDocumentiAllegato.setEnte(getEnte());
		elencoDocumentiAllegato.setStatoOperativoElencoDocumenti(StatoOperativoElencoDocumenti.BOZZA);
		// Subdocumenti relativi all'elenco
		elencoDocumentiAllegato.getSubdocumenti().addAll(createListSubdocumenti());
	}
	
	/**
	 * Crea una collection di subdocumenti di spesa a partire dalla lista degli uid di spesa.
	 * 
	 * @return la collection creata
	 */
	private Collection<Subdocumento<?,?>> createListSubdocumenti() {
		Collection<Subdocumento<?,?>> collection = new ArrayList<Subdocumento<?,?>>();
		for(Entry<Integer, String> i : getMappaUidSubdocumenti().entrySet()) {
			Subdocumento<?, ?> s = "E".equals(i.getValue()) ? new SubdocumentoEntrata() : new SubdocumentoSpesa();
			s.setUid(i.getKey());
			collection.add(s);
		}
		return collection;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaTipoDocumento}.
	 * 
	 * @return la request creata
	 */
	public RicercaTipoDocumento creaRequestRicercaTipoDocumento() {
		RicercaTipoDocumento request = creaRequest(RicercaTipoDocumento.class);
		request.setEnte(getEnte());
		return request;
	}
	
	
	/**
	 * Crea una request per il servizio di {@link RicercaQuoteDaAssociare}.
	 * 
	 * @return la request creata
	 */
	public RicercaQuoteDaAssociare creaRequestRicercaQuoteDaAssociare() {
		RicercaQuoteDaAssociare request = creaRequest(RicercaQuoteDaAssociare.class);
		
		request.setEnte(getEnte());
		request.setParametriPaginazione(new ParametriPaginazione(0, ELEMENTI_PER_PAGINA_QUOTA));
		request.setBilancio(getBilancio());
		request.setTipoFamigliaDocumento(getTipoFamigliaDocumento());
		// Documento
		request.setTipoDocumento(impostaEntitaFacoltativa(getTipoDocumento()));
		request.setAnnoDocumento(getAnnoDocumento());
		request.setNumeroDocumento(getNumeroDocumento());
		request.setNumeroQuota(getNumeroSubdocumento());
		request.setDataEmissioneDocumento(getDataEmissioneDocumento());
		// Movimento
		request.setAnnoMovimento(getAnnoMovimento());
		request.setNumeroMovimento(getNumeroMovimento());
		// Soggetto
		request.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		// Atto Amministrativa
		if(getAttoAmministrativo() != null) {
			// I dati dell'atto amministrativo sono int, dunque potrebbero essere 0 per indicare i 'null'
			request.setAnnoProvvedimento(getAttoAmministrativo().getAnno() == 0 ? null : getAttoAmministrativo().getAnno());
			request.setNumeroProvvedimento(getAttoAmministrativo().getNumero() == 0 ? null : getAttoAmministrativo().getNumero());
		}
		request.setTipoAtto(impostaEntitaFacoltativa(getTipoAtto()));
		request.setStruttAmmContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
		
		// Il documento relativo alla quota deve essere in uno di questi stati: VALIDO, PARZIALMENTE LIQUIDATO, PARZIALMENTE EMESSO.
		request.setStatiOperativoDocumento(Arrays.asList(StatoOperativoDocumento.VALIDO, StatoOperativoDocumento.PARZIALMENTE_LIQUIDATO,
				StatoOperativoDocumento.PARZIALMENTE_EMESSO));
		// Devono essere necessariamente collegate ad un movimento e il movimento deve appartenere allo stesso getBilancio() su cui si sta operando.
		request.setCollegatoAMovimentoDelloStessoBilancio(Boolean.TRUE);
		// Non devono associate ad un provvedimento o ad un elenco di documenti.
		request.setAssociatoAProvvedimentoOAdElenco(Boolean.FALSE);
		// Devono avere importoDaPagare o importoDaIncassare diverso da zero.
		request.setImportoDaPagareZero(Boolean.FALSE);
		// Se la quota e' di spesa ed e' rilevante IVA (flagrilevanteIVA uguale a true) deve avere il numero di registrazione IVA valorizzato
		// altrimenti non si puo' visualizzare nell'elenco. Non e' necessario verificare se l'Ente gestisce oppure no l'IVA perche' il flag
		// puo' essere valorizzato a true solamente se l'Ente permette la gestione IVA.
		request.setRilevatiIvaConRegistrazioneONonRilevantiIva(Boolean.TRUE);
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaQuotaSpesa}.
	 * 
	 * @return la request creata
	 */
	public RicercaQuotaSpesa creaRequestRicercaQuotaSpesa() {
		RicercaQuotaSpesa request = creaRequest(RicercaQuotaSpesa.class);
		
		request.setEnte(getEnte());
		request.setParametriPaginazione(new ParametriPaginazione(0, ELEMENTI_PER_PAGINA_QUOTA));
		request.setBilancio(getBilancio());
		
		// Documento
		request.setTipoDocumento(impostaEntitaFacoltativa(getTipoDocumento()));
		request.setAnnoDocumento(getAnnoDocumento());
		request.setNumeroDocumento(getNumeroDocumento());
		request.setNumeroQuota(getNumeroSubdocumento());
		request.setDataEmissioneDocumento(getDataEmissioneDocumento());
		// Movimento
		request.setAnnoMovimento(getAnnoMovimento());
		request.setNumeroMovimento(getNumeroMovimento());
		// Soggetto
		request.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		// Atto Amministrativa
		if(getAttoAmministrativo() != null) {
			// I dati dell'atto amministrativo sono int, dunque potrebbero essere 0 per indicare i 'null'
			request.setAnnoProvvedimento(getAttoAmministrativo().getAnno() == 0 ? null : getAttoAmministrativo().getAnno());
			request.setNumeroProvvedimento(getAttoAmministrativo().getNumero() == 0 ? null : getAttoAmministrativo().getNumero());
		}
		request.setTipoAtto(impostaEntitaFacoltativa(getTipoAtto()));
		request.setStruttAmmContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
		
		// Il documento relativo alla quota deve essere in uno di questi stati: VALIDO, PARZIALMENTE LIQUIDATO, PARZIALMENTE EMESSO.
		request.setStatiOperativoDocumento(Arrays.asList(StatoOperativoDocumento.VALIDO, StatoOperativoDocumento.PARZIALMENTE_LIQUIDATO,
				StatoOperativoDocumento.PARZIALMENTE_EMESSO));
		// Devono essere necessariamente collegate ad un movimento e il movimento deve appartenere allo stesso getBilancio() su cui si sta operando.
		request.setCollegatoAMovimentoDelloStessoBilancio(Boolean.TRUE);
		// Non devono associate ad un provvedimento o ad un elenco di documenti.
		request.setAssociatoAProvvedimentoOAdElenco(Boolean.FALSE);
		// Devono avere importoDaPagare o importoDaIncassare diverso da zero.
		request.setImportoDaPagareZero(Boolean.FALSE);
		// Se la quota e' di spesa ed e' rilevante IVA (flagrilevanteIVA uguale a true) deve avere il numero di registrazione IVA valorizzato
		// altrimenti non si puo' visualizzare nell'elenco. Non e' necessario verificare se l'Ente gestisce oppure no l'IVA perche' il flag
		// puo' essere valorizzato a true solamente se l'Ente permette la gestione IVA.
		request.setRilevatiIvaConRegistrazioneONonRilevantiIva(Boolean.TRUE);
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaQuotaEntrata}.
	 * 
	 * @return la request creata
	 */
	public RicercaQuotaEntrata creaRequestRicercaQuotaEntrata() {
		RicercaQuotaEntrata request = creaRequest(RicercaQuotaEntrata.class);
		
		request.setEnte(getEnte());
		request.setBilancio(getBilancio());
		request.setParametriPaginazione(new ParametriPaginazione(0, ELEMENTI_PER_PAGINA_QUOTA));
		
		// Documento
		request.setTipoDocumento(impostaEntitaFacoltativa(getTipoDocumento()));
		request.setAnnoDocumento(getAnnoDocumento());
		request.setNumeroDocumento(getNumeroDocumento());
		request.setNumeroQuota(getNumeroSubdocumento());
		request.setDataEmissioneDocumento(getDataEmissioneDocumento());
		// Movimento
		request.setAnnoMovimento(getAnnoMovimento());
		if(getNumeroMovimento() != null) {
			// Il numero movimento e' un BigDecimal
			request.setNumeroMovimento(new BigDecimal(getNumeroMovimento().toString()));
		}
		// Soggetto
		request.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		// Atto Amministrativa
		if(getAttoAmministrativo() != null) {
			// I dati dell'atto amministrativo sono int, dunque potrebbero essere 0 per indicare i 'null'
			request.setAnnoProvvedimento(getAttoAmministrativo().getAnno() == 0 ? null : getAttoAmministrativo().getAnno());
			request.setNumeroProvvedimento(getAttoAmministrativo().getNumero() == 0 ? null : getAttoAmministrativo().getNumero());
		}
		request.setTipoAtto(impostaEntitaFacoltativa(getTipoAtto()));
		request.setStruttAmmContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
		
		// Il documento relativo alla quota deve essere in uno di questi stati: VALIDO, PARZIALMENTE LIQUIDATO, PARZIALMENTE EMESSO.
		request.setStatiOperativoDocumento(Arrays.asList(StatoOperativoDocumento.VALIDO, StatoOperativoDocumento.PARZIALMENTE_LIQUIDATO,
				StatoOperativoDocumento.PARZIALMENTE_EMESSO));
		// Devono essere necessariamente collegate ad un movimento e il movimento deve appartenere allo stesso getBilancio() su cui si sta operando.
		request.setCollegatoAMovimentoDelloStessoBilancio(Boolean.TRUE);
		// Non devono associate ad un provvedimento o ad un elenco di documenti.
		request.setAssociatoAProvvedimentoOAdElenco(Boolean.FALSE);
		// Devono avere importoDaPagare o importoDaIncassare diverso da zero.
		request.setImportoDaIncassareZero(Boolean.FALSE);
		
		return request;
	}

}
