/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documentoiva;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.documentoiva.InserisciDocumentoIvaSpesaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.TipoRegistrazioneIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.sirfelser.frontend.webservice.FatturaElettronicaService;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaDettaglioFatturaElettronica;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaDettaglioFatturaElettronicaResponse;
import it.csi.siac.sirfelser.model.FatturaFEL;
import it.csi.siac.sirfelser.model.RiepilogoBeniFEL;

/**
 * Classe di action per l'inserimento del Documento Iva Spesa.
 * 
 * @author Alessandro Marchino
 * @version 1.0.1 - 12/06/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class InserisciDocumentoIvaSpesaAction extends GenericDocumentoIvaSpesaAction<InserisciDocumentoIvaSpesaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1163571826338675652L;
	
	@Autowired private transient FatturaElettronicaService fatturaElettronicaService;
	
	@Override
	public void prepare() throws Exception {
		// Pulisco le liste
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Check caso d'uso applicabile
		checkCasoDUsoApplicabile(model.getTitolo());
		
		try {
			// Ricerca dettaglio documento spesa
			ricercaDettaglioDocumento();
			
			// Ricerca di dettaglio del soggetto
			caricaSoggetto();
			
			// Ricerca della quota se associata
			if(Boolean.TRUE.equals(model.getTipoSubdocumentoIvaQuota())) {
				//carico la quota da db
				ricercaDettaglioQuota();
			}
			
			// Caricamento valori default
			caricamentoValoriDefault();
			impostaFlagMovimentoResiduo();
			// Caricamento classificatori
			caricamentoListe(TipoFamigliaDocumento.SPESA);
			
			// Impostazione dati fattura FEL
			impostaDatiDaFatturaFEL();
		} catch (WebServiceInvocationFailureException e) {
			log.error("execute", "Errore nell'invocazione dei servizi per la preparazione dell'inserimento del DocumentoIvaSpesa: " + e.getMessage(), e);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}
		// Ho caricato tutto: sono pronto a mostrare la pagina
		return SUCCESS;
	}
	
	private void impostaFlagMovimentoResiduo() {
		model.setMovimentoResiduo(false);
		if(Boolean.TRUE.equals(model.getTipoSubdocumentoIvaQuota())){
			controllaMovimentoResiduo(model.getSubdocumento());
		}else{
			for(SubdocumentoSpesa subdocumentoSpesa : model.getDocumento().getListaSubdocumenti()){
				controllaMovimentoResiduo(subdocumentoSpesa);
			}
		}
		
	}

	private void controllaMovimentoResiduo(SubdocumentoSpesa subdocumento) {
		if(subdocumento.getImpegno() != null && subdocumento.getImpegno().getAnnoMovimento() < model.getBilancio().getAnno()){
			log.debug("controllaMovimentoResiduo", "movimento residuo!!!!!!!!!!!!!!!!!!");
			model.setMovimentoResiduo(true);
		}
	}

	/**
	 * Carica i valori di default per la maschera.
	 */
	private void caricamentoValoriDefault() {
		// Subdocumento
		SubdocumentoIvaSpesa subdocumentoIva = model.getSubdocumentoIva();
		if(subdocumentoIva == null) {
			subdocumentoIva = new SubdocumentoIvaSpesa();
			model.setSubdocumentoIva(subdocumentoIva);
		}
		//valore di default: data odierna
		Date now = new Date();
		subdocumentoIva.setDataRegistrazione(now);
		subdocumentoIva.setDataProtocolloDefinitivo(now);
		subdocumentoIva.setDataProtocolloProvvisorio(now);
		
		// TipoRegistrazioneIva
		TipoRegistrazioneIva tri = ComparatorUtils.findByCodice(model.getListaTipoRegistrazioneIva(), "01");
		subdocumentoIva.setTipoRegistrazioneIva(tri);
		
		// Aliquota
		AliquotaSubdocumentoIva aliquotaSubdocumentoIva = model.getAliquotaSubdocumentoIva();
		if(aliquotaSubdocumentoIva == null) {
			aliquotaSubdocumentoIva = new AliquotaSubdocumentoIva();
			model.setAliquotaSubdocumentoIva(aliquotaSubdocumentoIva);
		}
		
		model.setTipoRegistroIvaIntracomunitarioDocumento(TipoRegistroIva.VENDITE_IVA_IMMEDIATA);
	}
	
	/**
	 * Impostazione dei dati di default a partire dalla fattura elettronica.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void impostaDatiDaFatturaFEL() throws WebServiceInvocationFailureException {
		final String methodName = "impostaDatiDaFatturaFEL";
		if(model.getDocumento() == null || model.getDocumento().getFatturaFEL() == null) {
			// Non sono presenti dati della fattura FEL
			return;
		}
		//carico i dati della fattura FEL rtamite servizio
		FatturaFEL fatturaFEL = ottieniDettaglioFatturaFEL();
		
		log.debug(methodName, "Impostazione dei valori di default a partire dalla fattura FEL");
		impostaTipoRegistroIva(fatturaFEL);
		impostaDatiAliquota(fatturaFEL);
	}
	
	/**
	 * Caricamento del dettaglio della fattura FEL.
	 * 
	 * @return la fattura
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private FatturaFEL ottieniDettaglioFatturaFEL() throws WebServiceInvocationFailureException {
		final String methodName = "ottieniDettaglioFatturaFEL";
		
		RicercaDettaglioFatturaElettronica request = model.creaRequestRicercaDettaglioFatturaElettronica();
		logServiceRequest(request);
		RicercaDettaglioFatturaElettronicaResponse response = fatturaElettronicaService.ricercaDettaglioFatturaElettronica(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
		}
		// Nessun errore: trovata fattura
		FatturaFEL fatturaFEL = response.getFatturaFEL();
		log.debug(methodName, "Trovata fattura con id " + fatturaFEL.getIdFattura());
		return fatturaFEL;
	}

	/**
	 * Preimpostato con il valore
	 * <ul>
	 *     <li><code>ACQUISTI IVA IMMEDIATA</code> se il campo Esigibilit&agrave; IVA (entit&agrave; Riepilogo Beni) = <code>I</code>;</li>
	 *     <li><code>ACQUISTI IVA ESIGIBILITA DIFFERITA</code> se il campo Esigibilit&agrave; IVA (entit&agrave; Riepilogo Beni) = <code>D</code>.</li>
	 * </ul>
	 * 
	 * @param fatturaFEL la fattura
	 */
	private void impostaTipoRegistroIva(FatturaFEL fatturaFEL) {
		final String methodName = "impostaTipoRegistroIva";
		// Prendo il primo
		if(fatturaFEL.getRiepiloghiBeni() == null || fatturaFEL.getRiepiloghiBeni().isEmpty()) {
			//non ho dei riepiloghi beni, non devo impostare l'iva
			log.debug(methodName, "Nessun riepilogo beni disponibile");
			return;
		}
		// Prendo il primo
		RiepilogoBeniFEL riepilogoBeniFEL = fatturaFEL.getRiepiloghiBeni().get(0);
		
		TipoRegistroIva tipoRegistroIva = null;
		if(BilConstants.ESIGIBILITA_IVA_I.getConstant().equals(riepilogoBeniFEL.getEsigibilitaIva())) {
			tipoRegistroIva = TipoRegistroIva.ACQUISTI_IVA_IMMEDIATA;
		} else if(BilConstants.ESIGIBILITA_IVA_D.getConstant().equals(riepilogoBeniFEL.getEsigibilitaIva())) {
			tipoRegistroIva = TipoRegistroIva.ACQUISTI_IVA_DIFFERITA;
		}
		
		log.debug(methodName, "Esigibilita' Iva per il Riepilogo Beni = " + riepilogoBeniFEL.getEsigibilitaIva() + " => tipoRegistroIva = " + tipoRegistroIva);
		model.setTipoRegistroIva(tipoRegistroIva);
		
	}

	/**
	 * Inserire per ogni record presente nell'entit&agrave; Riepilogo Beni un MOVIMENTO IVA in elenco se vengono soddisfatte le seguenti condizioni:
	 * <ul>
	 *     <li>
	 *         Il codice presente nel campo Riepilogo Beni.Aliquota IVA (che &eacute; una percentuale) deve essere uguale ad un unico valore percentuale presente
	 *         tra i codici di Aliquota IVA del sistema contabile affinch&eacute; si possa identificare univocamente l'aliquota iva da inserire;
	 *     </li>
	 *     <li>
	 *         Ogni record con i dati preimpostati come indicato nelle celle a seguire deve soddisfare i controlli descritti al par. 2.8.7
	 *     </li>
	 *     <li>
	 *         Se queste condizioni non fossero soddisfatte il record e quindi il MOVIMENTO IVA non dovr&agrave; essere inserito nell'elenco ed il sistema dopo aver
	 *         verificato-inserito tutti i records presenti in Riepilogo Beni dovr&agrave; visualizzare il messaggio cos&iacute; modificato
	 *         <code>&lt;FIN_INF_0284, Dati inconsistenti da documento FEL, 'impossibile completare la tabella dei movimenti iva da documento FEL'&gt;</code>
	 *     </li>
	 * </ul>
	 * 
	 * @param fatturaFEL la fattura
	 */
	private void impostaDatiAliquota(FatturaFEL fatturaFEL) {
		boolean impostatiTuttiIDati = true;
		
		for(RiepilogoBeniFEL riepilogoBeniFEL : fatturaFEL.getRiepiloghiBeni()) {
			impostatiTuttiIDati = impostaDatiAliquota(riepilogoBeniFEL) && impostatiTuttiIDati;
		}
		
		if(!impostatiTuttiIDati) {
			addInformazione(ErroreFin.DATI_INCOSTISTENTI_DA_DOCUMENTO_FEL.getErrore("dei movimenti iva"));
		}
	}
	
	/**
	 * Imposta i dati dell'aliquota pe il singolo riepilogo dei beni FEL.
	 * 
	 * @param riepilogoBeniFEL il riepilogo da cui ottenere il dato per l'aliquota
	 * 
	 * @return <code>true</code> se il dato &eacute; stato impostato; <code>false</code> altrimenti
	 */
	private boolean impostaDatiAliquota(RiepilogoBeniFEL riepilogoBeniFEL) {
		final String methodName = "impostaDatiAliquota";
		log.debug(methodName, "RiepilogoBeni.progressivo = " + riepilogoBeniFEL.getProgressivo() + " , aliquotaIva = " + riepilogoBeniFEL.getAliquotaIva());
		AliquotaIva aliquotaIva = null;
		// Controllo se vi sia un'aliquota con dato valore
		for(AliquotaIva ai : model.getListaAliquotaIva()) {
			if(riepilogoBeniFEL.getAliquotaIva() != null && riepilogoBeniFEL.getAliquotaIva().compareTo(ai.getPercentualeAliquota()) == 0) {
				aliquotaIva = ai;
				break;
			}
		}
		
		if(aliquotaIva == null) {
			log.debug(methodName, "Nessuna aliquota trovata per RiepilogoBeni con progressivo= " + riepilogoBeniFEL.getProgressivo());
			return false;
		}
		
		log.debug(methodName, "Trovata aliquota iva con uid " + aliquotaIva.getUid() + " per riepilogo con progressivo " + riepilogoBeniFEL.getProgressivo());
		// Impostazione dei dati
		AliquotaSubdocumentoIva aliquotaSubdocumentoIva = new AliquotaSubdocumentoIva();
		// Aliquota
		aliquotaSubdocumentoIva.setAliquotaIva(aliquotaIva);
		
		// Imponibile SIAC-6724
		aliquotaSubdocumentoIva.setImponibile(riepilogoBeniFEL.getImponibileImportoNotNull().setScale(2, RoundingMode.HALF_DOWN));
		log.debug(methodName, "RiepilogoBeni.imponibile = " + riepilogoBeniFEL.getImponibileImporto() + " => aliquotaSubdocumentoIva.imponibile = " + aliquotaSubdocumentoIva.getImponibile());
		
		// Imposta -SIAC-6724
		BigDecimal imposta = riepilogoBeniFEL.getImpostaNotNull().add(riepilogoBeniFEL.getArrotondamentoNotNull()).setScale(2, RoundingMode.HALF_DOWN);
		aliquotaSubdocumentoIva.setImposta(imposta);
		log.debug(methodName, "RiepilogoBeni.imposta = " + riepilogoBeniFEL.getImposta() + ", riepilogoBeni.arrotondamento = " + riepilogoBeniFEL.getArrotondamento()
				+ " => aliquotaSubdocumentoIva.imposta = " + imposta);
		
		// Totale = imponibile + imposta
		BigDecimal totale = aliquotaSubdocumentoIva.getImponibile().add(aliquotaSubdocumentoIva.getImposta());
		aliquotaSubdocumentoIva.setTotale(totale);
		log.debug(methodName, "AliquotaSubdocumentoIva.imponibile = " + aliquotaSubdocumentoIva.getImponibile() + ", aliquotaSubdocumentoIva.imposta = " + aliquotaSubdocumentoIva.getImposta()
				+ " => aliquotaSubdocumentoIva.totale = " + aliquotaSubdocumentoIva.getTotale());
		
		BigDecimal percentualeIndetraibilita = aliquotaIva.getPercentualeIndetraibilita();
		BigDecimal percentualeDetraibilita = BilUtilities.BIG_DECIMAL_ONE_HUNDRED.subtract(aliquotaIva.getPercentualeIndetraibilita());
		
		// Imposta indetraibile
		BigDecimal impostaIndetraibile = aliquotaSubdocumentoIva.getImposta().multiply(percentualeIndetraibilita)
				.divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED);
		aliquotaSubdocumentoIva.setImpostaIndetraibile(impostaIndetraibile);
		log.debug(methodName, "AliquotaSubdocumentoIva.imposta = " + aliquotaSubdocumentoIva.getImposta()
				+ ", aliquotaSubdocumentoIva.aliquotaIva.percentualeIndetraibilita = " + percentualeIndetraibilita
				+ " => aliquotaSubdocumentoIva.impostaIndetraibile = " + impostaIndetraibile);
		
		// Imposta detraibile
		BigDecimal impostaDetraibile = aliquotaSubdocumentoIva.getImposta().multiply(percentualeDetraibilita)
				.divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED);
		aliquotaSubdocumentoIva.setImpostaDetraibile(impostaDetraibile);
		log.debug(methodName, "AliquotaSubdocumentoIva.imposta = " + aliquotaSubdocumentoIva.getImposta()
				+ ", aliquotaSubdocumentoIva.aliquotaIva.percentualeDetraibilita (100 - percentualeIndetraibilita) = " + percentualeDetraibilita
				+ " => aliquotaSubdocumentoIva.impostaDetraibile = " + impostaDetraibile);
		
		model.getListaAliquotaSubdocumentoIva().add(aliquotaSubdocumentoIva);
		return true;
	}

	/**
	 * Inserisce il documento Iva di spesa.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciDocumentoIva() {
		final String methodName = "inserisciDocumentoIva";
		
		InserisceSubdocumentoIvaSpesa request = model.creaRequestInserisceSubdocumentoIvaSpesa();
		logServiceRequest(request);
		InserisceSubdocumentoIvaSpesaResponse response = documentoIvaSpesaService.inserisceSubdocumentoIvaSpesa(request);
		logServiceResponse(response);
		
		if(response.hasErrori()) {
			// Ho degli errori
			log.info(methodName, "Errore nell'invocazione del servizio InserisceSubdocumentoIvaSpesa");
			addErrori(response);
			return INPUT;
		}
		// Documento IVA inserito
		SubdocumentoIvaSpesa sis = response.getSubdocumentoIvaSpesa();
		log.debug(methodName, "Inserimento del documentoIvaSpesa con uid " + sis.getUid() + " andata a buon fine");
		model.setSubdocumentoIva(sis);
		// Imposto l'uid nel model
		model.setUidSubdocumentoIva(sis.getUid());
		//imposto un messaggio all'utente che indichi che l'operazione e' andata a buon fine
		impostaInformazioneSuccesso();
		//imposto dei dati in sessione per un'eventuale action successiva
		setMessaggiInSessionePerActionSuccessiva();
		setInformazioniInSessionePerActionSuccessiva();
		
		// Distruggo il crumb corrente per evitare si reinserire un altro documento iva (JIRA SIAC-1280)
		dissolveCurrentCrumb();
		
		return SUCCESS;
	}
	
	/**
	 * Valida l'inserimento del Documento Iva.
	 */
	public void validateInserisciDocumentoIva() {
		final String methodName = "validateInserisciDocumentoIva";
		SubdocumentoIvaSpesa sis = model.getSubdocumentoIva();
		// Controllo i campi obbligatorii
		checkNotNullNorInvalidUid(sis.getTipoRegistrazioneIva(), "Tipo Registrazione");
		checkNotNull(model.getTipoRegistroIva(), "Tipo Registro Iva");
		checkNotNullNorInvalidUid(sis.getRegistroIva(), "Registro");
		
		// Se il tipoRegistro è ACQUISTI IVA ESIGIBILITA DIFFERITA o ACQUISTI IVA ESIGIBILITA IMMEDIATA
		boolean dataDef = (model.getTipoRegistroIva() == TipoRegistroIva.ACQUISTI_IVA_IMMEDIATA || model.getTipoRegistroIva() == TipoRegistroIva.CORRISPETTIVI)
								&& sis.getDataProtocolloDefinitivo() != null;
		boolean dataProv = model.getTipoRegistroIva() == TipoRegistroIva.ACQUISTI_IVA_DIFFERITA && sis.getDataProtocolloProvvisorio() != null;
		
		checkCondition(dataDef || dataProv || model.isRegistrazioneSenzaProtocollo(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Data protocollo"));
				
		//controllo che il registro non sia bloccato. valutare se fare controllo solo lato backend
		checkModificheARegistroAbilitate(sis.getRegistroIva());
		
		checkCondition(model.getListaAliquotaSubdocumentoIva() != null && !model.getListaAliquotaSubdocumentoIva().isEmpty(), ErroreFin.NON_CI_SONO_MOVIMENTI_IVA_ASSOCIATI.getErrore());
		
		TipoRegistroIva tri = model.getTipoRegistroIva();
		// Carica il RegistroIva
		if(tri != null) {
			try {
				caricaListaRegistroIva(tri, model.getAttivitaIva());
			} catch (WebServiceInvocationFailureException e) {
				log.debug("prepareInserisciDocumentoIva", e.getMessage());
			}
		}
		
		// Validazione data protocollo
		Date dataProtocollo = (tri == TipoRegistroIva.ACQUISTI_IVA_IMMEDIATA || tri == TipoRegistroIva.CORRISPETTIVI)? 
				sis.getDataProtocolloDefinitivo() :
				sis.getDataProtocolloProvvisorio();
		if(dataProtocollo != null) {
			checkCondition(!dataProtocollo.before(model.getDocumento().getDataEmissione()), ErroreFin.DATA_REGIST_ANTECEDENTE_DATA_EMISSIONE_DOC.getErrore());
		}
		// Controllo totali
		BigDecimal totaleParziale = model.getTotaleTotaleMovimentiIva();
		BigDecimal importo = Boolean.TRUE.equals(model.getTipoSubdocumentoIvaQuota()) ? model.getSubdocumento().getImporto() : model.getImportoRilevanteIva();
		log.debug(methodName, "totale parziale: " + totaleParziale);
		log.debug(methodName, "importo: " + importo);
		controlloImporti(totaleParziale, importo);
	}
	
	/**
	 * Carica i movimenti Iva relativi al subdocumento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaMovimentiIva() {
		return SUCCESS;
	}
	
	/**
	 * Prepare specifico per l'apertura del collapse dei MovimentiIva
	 */
	public void prepareApriCollapseMovimentiIva() {
		// Pulisco il Movimento Iva
		model.setAliquotaSubdocumentoIva(new AliquotaSubdocumentoIva());
		model.setPercentualeAliquotaIva(BigDecimal.ZERO);
		model.setPercentualeIndetraibilitaAliquotaIva(BigDecimal.ZERO);
	}
	
	/**
	 * Apre il collapse dei Movimenti Iva.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String apriCollapseMovimentiIva() {
		// Non faccio nulla: restituisco solo la pagina vuota
		return SUCCESS;
	}
	
	/**
	 * Popola il movimento e restituisce la pagina corretta.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String apriModaleMovimentiIva() {
		// Prendo il movimento iva relativo alla riga selezionata
		int riga = model.getRiga().intValue();
		AliquotaSubdocumentoIva aliquotaSubdocumentoIva = ReflectionUtil.deepClone(model.getListaAliquotaSubdocumentoIva().get(riga));
		model.setAliquotaSubdocumentoIva(aliquotaSubdocumentoIva);
		
		model.setPercentualeAliquotaIva(aliquotaSubdocumentoIva.getAliquotaIva().getPercentualeAliquota());
		model.setPercentualeIndetraibilitaAliquotaIva(aliquotaSubdocumentoIva.getAliquotaIva().getPercentualeIndetraibilita());
		return SUCCESS;
	}
	
	/**
	 * Prepare specifico per l'inserimento del MovimentiIva
	 */
	public void prepareInserisciMovimentiIva() {
		// Pulisco il Movimento Iva
		model.setAliquotaSubdocumentoIva(new AliquotaSubdocumentoIva());
	}
	
	/**
	 * Inserisce il Movimento Iva.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciMovimentiIva() {
		final String methodName = "inserisciMovimentiIva";
        validateMovimentiIva(model.getAliquotaSubdocumentoIva(), model.getListaAliquotaSubdocumentoIva(), false, model.getDocumento().getTipoDocumento().isNotaCredito());
		if(hasErrori()) {
			//si sono verificati errori, non posso continuare
			log.info(methodName, "Errore nella validazione dell'inserimento del movimento iva");
			return SUCCESS;
		}
		
		popolaAliquotaIva(model.getAliquotaSubdocumentoIva(), model.getListaAliquotaIva());
		// Non ho errori: aggiungo il movimento alla lista in sessione
		model.addAliquotaSubdocumentoIva(model.getAliquotaSubdocumentoIva());
		// Pulisco l'aliquota
		model.setAliquotaSubdocumentoIva(null);
		
		//imposto un messaggio all'utente che indichi che l'operazione e' andata a buon fine
		impostaInformazioneSuccesso();
		
		return SUCCESS;
	}
	
	/**
	 * Popola l'aliquota a partire dall'uid della stessa e dalla lista delle possibili.
	 * 
	 * @param aliquota l'aliquota da cui ottenere l'uid
	 * @param lista    la lista in cui cercare l'aliquota
	 */
	private void popolaAliquotaIva(AliquotaSubdocumentoIva aliquota, List<AliquotaIva> lista) {
		// Ottengo i dati dell'aliquota
		AliquotaIva aliquotaIva = ComparatorUtils.searchByUid(lista, aliquota.getAliquotaIva());
		aliquota.setAliquotaIva(aliquotaIva);
	}

	/**
	 * Prepare specifico per l'aggiornamento del MovimentiIva
	 */
	public void prepareAggiornaMovimentiIva() {
		// Pulisco il Movimento Iva
		model.setAliquotaSubdocumentoIva(null);
		model.setPercentualeAliquotaIva(BigDecimal.ZERO);
		model.setPercentualeIndetraibilitaAliquotaIva(BigDecimal.ZERO);
	}
	
	/**
	 * Aggiorna il Movimento Iva.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaMovimentiIva() {
		final String methodName = "aggiornaMovimentiIva";
		validateMovimentiIva(model.getAliquotaSubdocumentoIva(), model.getListaAliquotaSubdocumentoIva(), true, model.getDocumento().getTipoDocumento().isNotaCredito());
		if(hasErrori()) {
			log.info(methodName, "Errore nella validazione dell'aggiornamento del movimento iva");
			return SUCCESS;
		}
		// Popolo l'aliquota
		popolaAliquotaIva(model.getAliquotaSubdocumentoIva(), model.getListaAliquotaIva());
		// Non ho errori: sostituisco il movimento nella lista in sessione
		int indiceDaEliminare = model.getRiga().intValue();
		model.getListaAliquotaSubdocumentoIva().remove(indiceDaEliminare);
		model.addAliquotaSubdocumentoIva(model.getAliquotaSubdocumentoIva());
		
		//imposto un messaggio all'utente che indichi che l'operazione e' andata a buon fine
		impostaInformazioneSuccesso();
		
		return SUCCESS;
	}
	
	/**
	 * Elimina il Movimento Iva.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String eliminaMovimentiIva() {
		int indiceDaEliminare = model.getRiga().intValue();
		// Elimino il movimento dalla lista in sessione
		model.getListaAliquotaSubdocumentoIva().remove(indiceDaEliminare);
		
		//imposto un messaggio all'utente che indichi che l'operazione e' andata a buon fine
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
}
