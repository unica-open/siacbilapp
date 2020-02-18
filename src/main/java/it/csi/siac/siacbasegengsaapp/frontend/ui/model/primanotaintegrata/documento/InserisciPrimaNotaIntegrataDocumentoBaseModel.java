/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.documento;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoQuotaRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinDocumentoHelper;
import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionUtil;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaRegistrazioneMovFin;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * Classe base di model per l'inserimento della prima nota integrata collegata al documento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/05/2015
 * @version 1.1.0 - 14/10/2015 - gestione GEN/GSA
 * 
 * @param <D>  la tipizzazione del documento
 * @param <S>  la tipizzazione del subdocumento
 * @param <SI> la tipizzazione del subdocumento iva
 * @param <E>  la tipizzazione del wrapper
 * 
 * @param <D1> la tipizzazione del documento
 * @param <S1> la tipizzazione del subdocumento
 * @param <SI1> la tipizzazione del subdocumento iva
 * @param <H> la tipizzazione dell'helper
 */
public abstract class InserisciPrimaNotaIntegrataDocumentoBaseModel<D extends Documento<?, ?>, S extends Subdocumento<?, ?>, SI extends SubdocumentoIva<?, ?, ?>,
		E extends ElementoQuotaRegistrazioneMovFin<?, ?>,
		// XXX: per gestione generics con Struts2, vedere se si puo' rifattorizzare
		D1 extends Documento<S1, SI1>, S1 extends Subdocumento<D1, SI1>, SI1 extends SubdocumentoIva<D1, S1, SI1>,
		H extends ConsultaRegistrazioneMovFinDocumentoHelper<D1, S1, SI1>>
		extends BaseInserisciAggiornaPrimaNotaIntegrataBaseModel<D1, H>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2076879467956312694L;

	// Pagina documento
	private Integer uidRegistrazione;
	
	private D documento;
	private S subdocumento;
	private SI subdocumentoIva;
	
	private MovimentoEP movimentoEP;
	
	private String descrRichiesta;
	private String documentiCollegati;
	private String noteCredito;
	
	// Liste
	private List<E> listaElementoQuota = new ArrayList<E>();
	private List<OperazioneSegnoConto> listaOperazioneSegnoConto = new ArrayList<OperazioneSegnoConto>();
	private List<RegistrazioneMovFin> listaRegistrazioneMovFin = new ArrayList<RegistrazioneMovFin>();
	private List<SI> listaSubdocumentoIva = new ArrayList<SI>();
	
	// SIAC-6062
	private CausaleEP causaleEPOriginaria;
	
	
	/** Costruttore vuoto di default */
	public InserisciPrimaNotaIntegrataDocumentoBaseModel() {
		setTitolo("Gestione Registro Richieste");
	}
	
	/**
	 * @return the uidRegistrazione
	 */
	public Integer getUidRegistrazione() {
		return uidRegistrazione;
	}

	/**
	 * @param uidRegistrazione the uidRegistrazione to set
	 */
	public void setUidRegistrazione(Integer uidRegistrazione) {
		this.uidRegistrazione = uidRegistrazione;
	}

	/**
	 * @return the documento
	 */
	public D getDocumento() {
		return documento;
	}

	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(D documento) {
		this.documento = documento;
	}

	/**
	 * @return the subdocumento
	 */
	public S getSubdocumento() {
		return subdocumento;
	}

	/**
	 * @param subdocumento the subdocumento to set
	 */
	public void setSubdocumento(S subdocumento) {
		this.subdocumento = subdocumento;
	}
	
	/**
	 * @return the uidSubdocumento
	 */
	public int getUidSubdocumento() {
		// XXX: workaround per problematiche di OGNL nell'injezione di campi
		return subdocumento != null ? subdocumento.getUid() : 0;
	}

	/**
	 * @return the subdocumentoIva
	 */
	public SI getSubdocumentoIva() {
		return subdocumentoIva;
	}

	/**
	 * @param subdocumentoIva the subdocumentoIva to set
	 */
	public void setSubdocumentoIva(SI subdocumentoIva) {
		this.subdocumentoIva = subdocumentoIva;
	}

	/**
	 * @return the movimentoEP
	 */
	public MovimentoEP getMovimentoEP() {
		return movimentoEP;
	}

	/**
	 * @param movimentoEP the movimentoEP to set
	 */
	public void setMovimentoEP(MovimentoEP movimentoEP) {
		this.movimentoEP = movimentoEP;
	}

	@Override
	public String getDescrRichiesta() {
		return descrRichiesta;
	}

	/**
	 * @param descrRichiesta the descrRichiesta to set
	 */
	public void setDescrRichiesta(String descrRichiesta) {
		this.descrRichiesta = descrRichiesta;
	}

	/**
	 * @return the documentiCollegati
	 */
	public String getDocumentiCollegati() {
		return documentiCollegati;
	}

	/**
	 * @param documentiCollegati the documentiCollegati to set
	 */
	public void setDocumentiCollegati(String documentiCollegati) {
		this.documentiCollegati = documentiCollegati;
	}

	/**
	 * @return the noteCredito
	 */
	public String getNoteCredito() {
		return noteCredito;
	}

	/**
	 * @param noteCredito the noteCredito to set
	 */
	public void setNoteCredito(String noteCredito) {
		this.noteCredito = noteCredito;
	}

	/**
	 * @return the listaElementoQuota
	 */
	public List<E> getListaElementoQuota() {
		return listaElementoQuota;
	}

	/**
	 * @param listaElementoQuota the listaElementoQuota to set
	 */
	public void setListaElementoQuota(List<E> listaElementoQuota) {
		this.listaElementoQuota = listaElementoQuota != null ? listaElementoQuota : new ArrayList<E>();
	}

	/**
	 * @return the listaOperazioneSegnoConto
	 */
	public List<OperazioneSegnoConto> getListaOperazioneSegnoConto() {
		return listaOperazioneSegnoConto;
	}

	/**
	 * @param listaOperazioneSegnoConto the listaOperazioneSegnoConto to set
	 */
	public void setListaOperazioneSegnoConto(List<OperazioneSegnoConto> listaOperazioneSegnoConto) {
		this.listaOperazioneSegnoConto = listaOperazioneSegnoConto != null ? listaOperazioneSegnoConto : new ArrayList<OperazioneSegnoConto>();
	}

	/**
	 * @return the listaRegistrazioneMovFin
	 */
	public List<RegistrazioneMovFin> getListaRegistrazioneMovFin() {
		return listaRegistrazioneMovFin;
	}

	/**
	 * @param listaRegistrazioneMovFin the listaRegistrazioneMovFin to set
	 */
	public void setListaRegistrazioneMovFin(List<RegistrazioneMovFin> listaRegistrazioneMovFin) {
		this.listaRegistrazioneMovFin = listaRegistrazioneMovFin != null ? listaRegistrazioneMovFin : new ArrayList<RegistrazioneMovFin>();
	}

	/**
	 * @return the listaSubdocumentoIva
	 */
	public List<SI> getListaSubdocumentoIva() {
		return listaSubdocumentoIva;
	}

	/**
	 * @param listaSubdocumentoIva the listaSubdocumentoIva to set
	 */
	public void setListaSubdocumentoIva(List<SI> listaSubdocumentoIva) {
		this.listaSubdocumentoIva = listaSubdocumentoIva != null ? listaSubdocumentoIva : new ArrayList<SI>();
	}

	/**
	 * @return the causaleEPOriginaria
	 */
	public CausaleEP getCausaleEPOriginaria() {
		return this.causaleEPOriginaria;
	}

	/**
	 * @param causaleEPOriginaria the causaleEPOriginaria to set
	 */
	public void setCausaleEPOriginaria(CausaleEP causaleEPOriginaria) {
		this.causaleEPOriginaria = causaleEPOriginaria;
	}

	@Override
	public boolean isAggiornamento() {
		return getPrimaNota() != null && getPrimaNota().getUid() != 0;
	}
	
	/**
	 * @return the baseUrlSubdocumento
	 */
	public abstract String getBaseUrlSubdocumento();
	
	/**
	 * @return the actionMethodName
	 */
	public String getActionMethodName() {
		return isAggiornamento() ? "aggiornaPrimaNota" : "inserisciPrimaNota";
	}
	
	
	@Override
	public String getUrlBackToStep1() {
		return getBaseUrlSubdocumento() + "_backToStep1";
	}
	
	/**
	 * @return the testoPulsanteSubmit
	 */
	public String getTestoPulsanteSubmit() {
		return isValidazione()
			? "valida"
			: isAggiornamento()
				? "aggiorna"
				: "inserisci";
	}
	
	/**
	 * @return the intestazionePagina
	 */
	public String getIntestazionePagina() {
		return isValidazione()
			? "Completa e valida richiesta"
			: "Inserimento prima nota integrata";
	}
	
	/**
	 * Gets the uid documento.
	 *
	 * @return the uid documento
	 */
	//SIAC-5333
	public int getUidDocumento() {
		return documento != null? documento.getUid() : 0;
	}
	
	/* **** Requests **** */
	
	@Override
	public RicercaDettaglioPrimaNotaIntegrata creaRequestRicercaDettaglioPrimaNotaIntegrata() {
		RicercaDettaglioPrimaNotaIntegrata request = creaRequest(RicercaDettaglioPrimaNotaIntegrata.class);
		
		RegistrazioneMovFin rmf = new RegistrazioneMovFin();
		//rmf.setUid(getRegistrazioneMovFin().getUid());
		rmf.setAmbito(getAmbito());
		
		request.setDocumento(documento);
		//devo indicare da che evento parto per distinguere le prime note della cassa economale da quelle del doc standard
		request.setEvento(getRegistrazioneMovFin().getEvento());
		request.setRegistrazioneMovFin(rmf);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioRegistrazioneMovFin}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioRegistrazioneMovFin creaRequestRicercaDettaglioRegistrazioneMovFin() {
		RicercaDettaglioRegistrazioneMovFin request = creaRequest(RicercaDettaglioRegistrazioneMovFin.class);
		
		RegistrazioneMovFin registrazione = new RegistrazioneMovFin();
		registrazione.setUid(getUidRegistrazione());
		registrazione.setBilancio(getBilancio());
		registrazione.setAmbito(getAmbito());
		
		request.setRegistrazioneMovFin(registrazione);
	
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaRegistrazioneMovFin}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaRegistrazioneMovFin creaRequestRicercaSinteticaRegistrazioneMovFin() {
		RicercaSinteticaRegistrazioneMovFin request = creaRequest(RicercaSinteticaRegistrazioneMovFin.class);
		
		// Trasformo a tutti gli effetti la ricerca sintetica in estesa. Sono una persona molto cattiva :-)
		request.setParametriPaginazione(creaParametriPaginazione(Integer.MAX_VALUE));
		
		request.setIdDocumento(getDocumento().getUid());
		
		// Creo una nuova registrazione in cui includo solo il getBilancio()
		RegistrazioneMovFin rmf = new RegistrazioneMovFin();
		rmf.setBilancio(getBilancio());
		rmf.setAmbito(getAmbito());
		request.setRegistrazioneMovFin(rmf);
		//devo indicare da che evento parto per distinguere le registrazioni della cassa economale da quelle del doc standard
		request.setEventoRegistrazioneIniziale(getRegistrazioneMovFin().getEvento());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RegistraPrimaNotaIntegrata}.
	 * 
	 * @param mayValidate Se la registrazione pu&ograve; essere validabile
	 * @return la request creata
	 */
	public RegistraPrimaNotaIntegrata creaRequestRegistraPrimaNotaIntegrata(boolean mayValidate) {
		RegistraPrimaNotaIntegrata request = creaRequest(RegistraPrimaNotaIntegrata.class);
		
		boolean validatable = mayValidate && isValidazione();
		request.setIsDaValidare(Boolean.valueOf(validatable));
		request.setIsAggiornamento(Boolean.valueOf(isAggiornamento()));
		
		// Popolamento dati prima nota
		getPrimaNota().setTipoCausale(TipoCausale.Integrata);
		getPrimaNota().setBilancio(getBilancio());
		getPrimaNota().setEnte(getEnte());
		// SIAC-5336
		if(isValidazione() && Ambito.AMBITO_GSA.equals(getAmbito())) {
			// Aggiunto solo per GSA
			request.setClassificatoreGSA(impostaEntitaFacoltativa(getClassificatoreGSA()));
		}
		
		List<MovimentoEP> movimentiEP = calcolaListaMovimentoEPPrimaNota();
		
		getPrimaNota().setListaMovimentiEP(movimentiEP);
		getPrimaNota().setAmbito(getAmbito());
		
		request.setPrimaNota(getPrimaNota());
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link AggiornaPrimaNota}.
	 * 
	 * @return la request creata
	 */
	public AggiornaPrimaNota creaRequestAggiornaPrimaNota() {
		AggiornaPrimaNota request = creaRequest(AggiornaPrimaNota.class);
		
		getPrimaNota().setTipoCausale(TipoCausale.Integrata);
		getPrimaNota().setBilancio(getBilancio());
		getPrimaNota().setEnte(getEnte());
		
		List<MovimentoEP> movimentiEP = calcolaListaMovimentoEPPrimaNota();
		
		getPrimaNota().setListaMovimentiEP(movimentiEP);
		getPrimaNota().setAmbito(getAmbito());
		
		request.setPrimaNota(getPrimaNota());
		
		return request;
	}
	
	@Override
	public RegistraPrimaNotaIntegrata creaRequestRegistraPrimaNotaIntegrataPerCheckEsecuzione() {
		RegistraPrimaNotaIntegrata request = creaRequest(RegistraPrimaNotaIntegrata.class);
		
		List<MovimentoEP> lmep = new ArrayList<MovimentoEP>();
		
		for(RegistrazioneMovFin rmf : getListaRegistrazioneMovFin()) {
			MovimentoEP mep = new MovimentoEP();
			mep.setRegistrazioneMovFin(rmf);
			lmep.add(mep);
		}
		
		PrimaNota pn = new PrimaNota();
		pn.setListaMovimentiEP(lmep);
		
		request.setPrimaNota(pn);
		request.setCheckOnlyElaborazioneAttiva(Boolean.TRUE);
		return request;
	}
	
	/**
	 * Calcolo della lista dei movimenti EP da associare alla prima nota.
	 * 
	 * @return la lista dei movimenti EP
	 */
	private List<MovimentoEP> calcolaListaMovimentoEPPrimaNota() {
		List<MovimentoEP> movimentiEP = new ArrayList<MovimentoEP>();
		List<E> listaWrapper = ReflectionUtil.deepClone(getListaElementoQuota());
		
		// Se sono dall'inserimento o nell'aggiornamento del dettaglio, rimpiazzo eventualmente il dato
		if(getMovimentoEP() != null && getMovimentoEP().getCausaleEP() != null && getMovimentoEP().getCausaleEP().getUid() != 0 && getSubdocumento() != null && getSubdocumento().getUid() != 0) {
			for(E wrapper : listaWrapper) {
				// Lotto N: avevo un boolean su 'wrapper.hasDatiMovimento', ma non ricordo come mai fosse presente ne' perche' non funzionasse correttamente => eliminato
				if(wrapper.getRegistrazioneMovFin() != null && wrapper.getRegistrazioneMovFin().getUid() != 0
						&& wrapper.getSubdocumento() != null && getSubdocumento().getUid() == wrapper.getSubdocumento().getUid()) {
					// Ho lo stesso elemento: rimpiazzo i dati con i nuovi
					getMovimentoEP().setEnte(getEnte());
					
					List<MovimentoEP> list = new ArrayList<MovimentoEP>();
					list.add(getMovimentoEP());
					wrapper.getRegistrazioneMovFin().setListaMovimentiEP(list);
					wrapper.setMovimentoEP(getMovimentoEP());
					// Ho trovato il valore che mi interessa: esco dal ciclo
					break;
				}
			}
		}
		
		// Imposto i wrapper nella lista
		for(E eqrmf : listaWrapper) {
			RegistrazioneMovFin registrazioneMovFin = eqrmf.getRegistrazioneMovFin();
			MovimentoEP movEP = eqrmf.getMovimentoEP();
			if(registrazioneMovFin == null || registrazioneMovFin.getUid() == 0 || movEP == null
					|| (movEP.getUid() == 0 && (movEP.getCausaleEP() == null || movEP.getCausaleEP().getUid() == 0))) {
				// Ignoro il wrapper: non ho la registrazione o il movimento EP
				continue;
			}
			
			// Injetto una mini-registrazione, si' da evitare il ciclo
			RegistrazioneMovFin rmf = new RegistrazioneMovFin();
			rmf.setUid(registrazioneMovFin.getUid());
			rmf.setAmbito(getAmbito());
			rmf.setEnte(getEnte());
			movEP.setRegistrazioneMovFin(rmf);
			movEP.setAmbito(getAmbito());
			movEP.setEnte(getEnte());
			
			// Ricalcolo il numero di riga
			int numeroRiga = 0;
			for(MovimentoDettaglio movimentoDettaglio : movEP.getListaMovimentoDettaglio()) {
				movimentoDettaglio.setAmbito(getAmbito());
				movimentoDettaglio.setNumeroRiga(Integer.valueOf(numeroRiga));
				movimentoDettaglio.setEnte(getEnte());
				numeroRiga++;
			}
			movimentiEP.add(movEP);
		}
		return movimentiEP;
	}
	
}
